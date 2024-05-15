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
	ANIME_DETAILS_PATH: `${SERVER_BASE_URL}/anime/{id}/details`,
	ANIME_INFO_PATH: `${SERVER_BASE_URL}/anime/{id}/info`,
	ANIME_LIKE_PATH: `${SERVER_BASE_URL}/anime/{id}/like`,
	ANIME_DISLIKE_PATH: `${SERVER_BASE_URL}/anime/{id}/dislike`,
	ANIME_ADD_LIBRARY_PATH: `${SERVER_BASE_URL}/anime/{id}/library/add`,
	ANIME_REMOVE_LIBRARY_PATH: `${SERVER_BASE_URL}/anime/{id}/library/remove`,
	ANIME_CONSTANT_PATH: `${SERVER_BASE_URL}/constant`,
	ANIME_SEARCH_PATH: `${SERVER_BASE_URL}/anime/search`,
	ANIME_BANNER_PATH: `${SERVER_BASE_URL}/anime/banner`,
	ANIME_RECENT_PATH: `${SERVER_BASE_URL}/anime/recent`,
	ANIME_POPULAR_PATH: `${SERVER_BASE_URL}/anime/popular`,
	ANIME_TRENDING_PATH: `${SERVER_BASE_URL}/anime/trending`,
	ANIME_GENRE_PATH: `${SERVER_BASE_URL}/anime/genre`,
	ANIME_RECOMMENDATION_PATH: `${SERVER_BASE_URL}/anime/{id}/recommendations`,
	ANIME_EPISODE_DETAILS_PATH: `${SERVER_BASE_URL}/anime/{id}/episode`,
	ANIME_EPISODE_PATH: `${SERVER_BASE_URL}/anime/{id}/episode/{episodeNumber}`,
	NEWS_PATH: `${SERVER_BASE_URL}/news`,
	NEWS_DETAILS_PATH: `${SERVER_BASE_URL}/news/{id}`
};

export const CLIENT_URLS = {
	LOGIN_URL: '/login',
	BROWSE_URL: '/browse',
	LIBRARY_URL: '/library',
	NEWS_URL: '/news',
	PROFILE_URL: '/profile',
	SETTINGS_URL: '/settings',
	BUG_REPORT_URL: '/bug-report',
	SEARCH_URL: '/search'
};
