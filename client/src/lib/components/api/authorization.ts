import client from './client';

export const authorize = async (input: LoginInput) => {
	try {
		const response: AuthorizeResponse = await client.fetchData('POST', '/api/auth/authorize', input);
		return response;
	} catch (error) {
		return Promise.reject(error);
	}
};

export function expiresIn(jwt: string, expiresIn: number = 1000 * 60 * 5): boolean {
	const payload: JwtPayload = JSON.parse(atob(jwt.split('.')[1]));
	const expiration = new Date(payload.exp);
	const now = new Date();
	return expiration.getTime() - now.getTime() < expiresIn ? true : false;
}
