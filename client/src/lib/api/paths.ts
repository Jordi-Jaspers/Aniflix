// Base URL for the API
const SERVER_BASE_URL: string = import.meta.env.VITE_SERVER_BASE_URL + '/api';


export const SERVER_URLS = {
    SERVER_BASE_URL,
    AUTH_PATH: `${SERVER_BASE_URL}/auth`,
    REGISTER_PATH: `${SERVER_BASE_URL}/auth/register`,
    AUTHORIZE_PATH: `${SERVER_BASE_URL}/auth/authorize`,
    REFRESH_PATH: `${SERVER_BASE_URL}/auth/token`,
    USER_PATH: `${SERVER_BASE_URL}/user`,
    USER_DETAILS_PATH: `${SERVER_BASE_URL}/user/details`,
    ANIME_PATH: `${SERVER_BASE_URL}/anime`,
    ANIME_DETAILS_PATH: `${SERVER_BASE_URL}/anime/{id}`,
    ANIME_CONSTANT_PATH: `${SERVER_BASE_URL}/constant`,
    ANIME_BANNER_PATH: `${SERVER_BASE_URL}/anime/banner`,
    ANIME_RECENT_PATH: `${SERVER_BASE_URL}/anime/recent`,
    ANIME_POPULAR_PATH: `${SERVER_BASE_URL}/anime/popular`,
    ANIME_TRENDING_PATH: `${SERVER_BASE_URL}/anime/trending`,
    ANIME_GENRE_PATH: `${SERVER_BASE_URL}/anime/genre`
};

export const CLIENT_URLS = {
    LOGIN_URL: '/login',
    BROWSE_URL: '/browse',
};



