import {useAuthenticated, useHasAuthError} from '$lib/components/store/store';
import {goto} from '$app/navigation';
import {CLIENT_URLS, SERVER_URLS} from '$lib/api/paths';
import toast from 'svelte-french-toast';
import {accessToken} from "$lib/components/store/sessionstorage";
import {refreshToken} from "$lib/components/store/localstorage";
import {get} from "svelte/store";
import {closeModal} from "$lib/api/util";

const ISSUER: string = import.meta.env.VITE_SERVER_ISSUER;

/**
 * Function to authorize a user on the server.
 *
 * @param input The login request.
 */
export async function authorize(input: LoginRequest): Promise<AuthorizeResponse | string> {
    const response: Response = await fetch(SERVER_URLS.AUTHORIZE_PATH, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
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
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(input)
    });

    return response.ok ? await response.json() : getErrorMessage(response);
}

export async function logout(): Promise<void> {
    closeModal();
    removeTokens();
    await goto(CLIENT_URLS.LOGIN_URL);
}

/**
 * Function to validate the account of a user.
 *
 * @param token The validation token.
 * @returns A promise which resolves to void.
 */
export async function validateAccount(token: string): Promise<void> {
    const response: Response = await fetch(SERVER_URLS.VALIDATE_PATH + '?token=' + token, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}
    });

    if (response.ok) {
        toast.success('Account successfully activated! Have fun streaming.', {
            duration: 5000,
            position: 'bottom-center',
            style: 'background: #262626; color: #ffffff;'
        });

        const data: AuthorizeResponse = await response.json();
        updateTokens(data);

        await goto(CLIENT_URLS.BROWSE_URL);
    } else {
        toast.error(await getErrorMessage(response), {
            duration: 5000,
            position: 'bottom-center',
            style: 'background: #262626; color: #ffffff;'
        });

        await goto(CLIENT_URLS.LOGIN_URL);
    }
}

/**
 * Function which automatically adds the access token to the request headers.
 * If the access token is expired or not available, it will attempt to refresh the token and retry the request.
 *
 * @param endpoint The API endpoint to call.
 * @param options The request options.
 */
export async function curl(endpoint: string, options: RequestInit = {}): Promise<Response> {
    if (!get(accessToken) || !isValid(get(accessToken)) || expiresIn(get(accessToken))) {
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
        }
    }

    const headers = new Headers(options.headers);
    headers.append('Authorization', `Bearer ${get(accessToken)}`);
    return await fetchWithRetry(endpoint, options, headers);
}

/**
 * Function which fetches the data from the server and retries the request if it fails.
 * @param endpoint The API endpoint to call.
 * @param options The request options.
 * @param headers The request headers.
 */
async function fetchWithRetry(endpoint: string, options: RequestInit, headers: Headers) {
    const response: Response = await fetch(endpoint, {...options, headers});
    if (response.ok || response.status === 401) {
        return response;
    } else {
        const retryResponse: Response = await fetch(endpoint, {...options, headers});
        if (retryResponse.ok || retryResponse.status === 401) {
            return retryResponse;
        } else {
            toast.error(await getErrorMessage(retryResponse), {
                duration: 5000,
                position: 'bottom-center',
                style: 'background: #262626; color: #ffffff;'
            });
            return retryResponse;
        }
    }
}

/**
 * Check if the user is authenticated.
 */
export async function isUserAuthenticated(): Promise<boolean> {
    // Check if access token is valid and not expired.
    if (get(accessToken) && isValid(get(accessToken)) && !expiresIn(get(accessToken))) {
        return true;
    }

    // Check if refresh token is valid.
    if (get(refreshToken) && isValid(get(refreshToken))) {
        const response = await refreshTokens();
        if (typeof response !== 'string') {
            return true;
        }

        toast.error('You are unauthorized or your session has expired.', {
            duration: 5000,
            position: 'bottom-center',
            style: 'background: #262626; color: #ffffff;'
        });
    }

    return false;
}

/**
 * Function which refreshes the access token using the refresh token by sending a request to the server.
 */
async function refreshTokens(): Promise<AuthorizeResponse | string> {
    const response: Response = await fetch(SERVER_URLS.REFRESH_PATH, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({refreshToken: get(refreshToken)})
    });

    if (!response.ok) {
        removeTokens();
        useHasAuthError.set(true);
        return getErrorMessage(response);
    } else {
        const data: AuthorizeResponse = await response.json();
        updateTokens(data);
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
    accessToken.set('');
    refreshToken.set('');
}

/**
 * Function to update the access and refresh tokens in local storage.
 *
 * @param response The response from the server.
 */
function updateTokens(response: AuthorizeResponse) {
    useAuthenticated.set(true);
    useHasAuthError.set(false);
    accessToken.set(response.accessToken);
    refreshToken.set(response.refreshToken);
}
