import client from './client';

export const authorize = async (input: LoginInput) => {
	try {
		const response: AuthorizeResponse = await client.fetchData('POST', '/api/auth/authorize', input);
		return response;
	} catch (error) {
		return Promise.reject(error);
	}
};
