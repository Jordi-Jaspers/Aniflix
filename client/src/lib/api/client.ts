import { useAuthenticated, useHasAuthError } from '$lib/components/store/store';
import { goto } from '$app/navigation';
import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
import toast from 'svelte-french-toast';

const ISSUER: string = import.meta.env.VITE_SERVER_ISSUER;

/**
 * Function to authorize a user on the server.
 *
 * @param input The login request.
 */
export async function authorize(input: LoginRequest): Promise<AuthorizeResponse | string> {
	const response: Response = await fetch(SERVER_URLS.AUTHORIZE_PATH, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(input)
	});

	if (!response.ok) {
		return getErrorMessage(response);
	} else {
		const data: AuthorizeResponse = await response.json();
		updateTokens(data);
		return data;
	}
}

/**
 * Function to register a user within the application.
 *
 * @param input The register request.
 */
export async function register(input: RegisterRequest) {
	const response: Response = await fetch(SERVER_URLS.REGISTER_PATH, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(input)
	});

	return response.ok ? await response.json() : getErrorMessage(response);
}

export async function logout(): Promise<void> {
	removeTokens();
	await goto(CLIENT_URLS.LOGIN_URL);
}

/**
 * Function which automatically adds the access token to the request headers.
 * If the access token is expired or not available, it will attempt to refresh the token and retry the request.
 *
 * @param endpoint The API endpoint to call.
 * @param options The request options.
 */
export async function curl(endpoint: string, options: RequestInit = {}): Promise<Response> {
	let accessToken = localStorage.getItem('ANIFLIX_ACCESS_TOKEN');
	if (!accessToken || !isValid(accessToken) || expiresIn(accessToken)) {
		const tokens = await refreshTokens();
		if (typeof tokens === 'string') {
			await goto(CLIENT_URLS.LOGIN_URL);
			toast.error(tokens, {
				duration: 5000,
				position: 'bottom-center',
				style: 'background: #262626; color: #ffffff;'
			});
		} else {
			updateTokens(tokens);
			accessToken = localStorage.getItem('ANIFLIX_ACCESS_TOKEN');
		}
	}

	const headers = new Headers(options.headers);
	headers.append('Authorization', `Bearer ${accessToken}`);
	const response: Response = await fetch(endpoint, { ...options, headers });

	if (!response.ok && response.status !== 401) {
		toast.error(await getErrorMessage(response), {
			duration: 5000,
			position: 'bottom-center',
			style: 'background: #262626; color: #ffffff;'
		});
	}
	return response;
}

/**
 * Check if the user is authenticated.
 */
export async function isUserAuthenticated(): Promise<boolean> {
	// Check if access token is valid and not expired.
	const accessToken = localStorage.getItem('ANIFLIX_ACCESS_TOKEN');
	console.log('Checking if there is an access token: ' + accessToken);
	if (accessToken && isValid(accessToken) && !expiresIn(accessToken)) {
		console.log('Access token is valid and not expired.');
		return true;
	}

	// Check if refresh token is valid.
	const refreshToken = localStorage.getItem('ANIFLIX_REFRESH_TOKEN');
	if (refreshToken && isValid(refreshToken)) {
		const response = await refreshTokens();
		if (typeof response !== 'string') {
			return true;
		}
	}

	toast.error('You are unauthorized or your session has expired.', {
		duration: 5000,
		position: 'bottom-center',
		style: 'background: #262626; color: #ffffff;'
	});

	return false;
}

/**
 * Function which refreshes the access token using the refresh token by sending a request to the server.
 */
async function refreshTokens(): Promise<AuthorizeResponse | string> {
	const refreshToken: string | null = localStorage.getItem('ANIFLIX_REFRESH_TOKEN');
	const response: Response = await fetch(SERVER_URLS.REFRESH_PATH, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ refreshToken })
	});

	if (!response.ok) {
		removeTokens();
		useHasAuthError.set(true);
		return getErrorMessage(response);
	} else {
		const data: AuthorizeResponse = await response.json();
		localStorage.setItem('ANIFLIX_ACCESS_TOKEN', data.accessToken);
		localStorage.setItem('ANIFLIX_REFRESH_TOKEN', data.refreshToken);
		useAuthenticated.set(true);
		useHasAuthError.set(false);
		return data;
	}
}

/**
 * Retrieve the error message from the exception in the server response.
 */
async function getErrorMessage(response: Response): Promise<string> {
	const exception: Exception | ValidationException = await response.json();
	if ('apiErrorReason' in exception) {
		return exception.statusMessage + ': ' + exception.apiErrorReason;
	} else if ('errorMessage' in exception) {
		return exception.statusMessage + ': ' + exception.errorMessage;
	} else if ('errors' in exception) {
		let errorMessages: string = 'The following fields contain errors:\n';
		exception.errors.forEach((error: ValidationField) => {
			errorMessages += `${error.field}: ${error.code}\n`;
		});
		return errorMessages;
	}
	return 'An unknown error occurred.';
}

/**
 * Function to check if the access token is expired or about to expire.
 * Default expiration time is 1 minute.
 *
 * @param jwt The access token.
 * @param expiresIn The time in seconds before the token expires.
 */
function expiresIn(jwt: string, expiresIn: number = 60): boolean {
	const payload: JwtPayload = JSON.parse(atob(jwt.split('.')[1]));
	const now: number = Math.floor(new Date().getTime() / 1000.0);
	return payload.exp + expiresIn - now <= 0;
}

/**
 * Check if the access token is valid and not expired.
 */
function isValid(jwt: string): boolean {
	const payload: JwtPayload = JSON.parse(atob(jwt.split('.')[1]));
	const now: number = Math.floor(new Date().getTime() / 1000.0);
	return payload.exp - now > 0 && payload.iss === ISSUER;
}

/**
 * Function to remove the access and refresh tokens from local storage.
 */
function removeTokens() {
	useAuthenticated.set(false);
	localStorage.removeItem('ANIFLIX_ACCESS_TOKEN');
	localStorage.removeItem('ANIFLIX_REFRESH_TOKEN');
}

/**
 * Function to update the access and refresh tokens in local storage.
 *
 * @param response The response from the server.
 */
function updateTokens(response: AuthorizeResponse) {
	useAuthenticated.set(true);
	useHasAuthError.set(false);
	localStorage.setItem('ANIFLIX_ACCESS_TOKEN', response.accessToken);
	localStorage.setItem('ANIFLIX_REFRESH_TOKEN', response.refreshToken);
}
