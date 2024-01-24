const BASE_URL = import.meta.env.VITE_SERVER_BASE_URL;

const fetchData = async (method: string, endpoint: string, body: any, token: string | undefined = undefined) => {
	const headers: HeadersInit = {
		'Content-Type': 'application/json'
	};

	if (token) {
		headers.Authorization = `Bearer ${token}`;
	}

	try {
		const response: Response = await fetch(BASE_URL + endpoint, {
			method,
			headers,
			body: JSON.stringify(body)
		});

		return response.ok ? response.json() : handleResponse(response);
	} catch (err) {
		return Promise.reject("Unhandled Error: Couldn't fetch data from the server.");
	}
};

async function handleResponse(response: Response) {
	const exception: Exception | ValidationException = await response.json();
	if ('apiErrorReason' in exception) {
		return Promise.reject(exception.apiErrorReason);
	} else if ('errors' in exception) {
		let errorMessages: string = 'The following fields contain errors:\n';
		exception.errors.forEach((error: ValidationField) => {
			errorMessages += `${error.field}: ${error.code}\n`;
		});
		return Promise.reject(errorMessages);
	}
	throw new Error();
}

const client = { fetchData };

export default client;
