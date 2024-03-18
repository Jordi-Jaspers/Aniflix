import {redirect} from '@sveltejs/kit';
import {CLIENT_URLS} from '$lib/api/paths';

export const load = async ({url}) => {
    if (url.pathname === '/') {
        throw redirect(303, CLIENT_URLS.LOGIN_URL);
    }
    return {};
};
