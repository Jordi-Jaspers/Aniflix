// Base URL for the API
export const SERVER_BASE_URL: string = import.meta.env.VITE_SERVER_BASE_URL + '/api';
export const SERVER_URLS = {
	SERVER_BASE_URL,
	AUTH_PATH: `${SERVER_BASE_URL}/auth`,
	REGISTER_PATH: `${SERVER_BASE_URL}/auth/register`,
	VALIDATE_PATH: `${SERVER_BASE_URL}/auth/validate`,
	AUTHORIZE_PATH: `${SERVER_BASE_URL}/auth/authorize`,
	REFRESH_PATH: `${SERVER_BASE_URL}/auth/token`,
	USER_PATH: `${SERVER_BASE_URL}/user`,
	USER_DETAILS_PATH: `${SERVER_BASE_URL}/user/details`,
	USER_DETAILS_EMAIL_PATH: `${SERVER_BASE_URL}/user/details/email`,
	EMAIL_VALIDATION_PATH: `${SERVER_BASE_URL}/user/details/email/validate`,
	ANIME_PATH: `${SERVER_BASE_URL}/anime`,
	ANIME_DETAILS_PATH: `${SERVER_BASE_URL}/anime/{id}/details`,
	ANIME_NEXT_AIRING_EPISODE_PATH: `${SERVER_BASE_URL}/anime/{id}/next_airing_episode`,
	ANIME_INFO_PATH: `${SERVER_BASE_URL}/anime/{id}/info`,
	ANIME_LIKE_PATH: `${SERVER_BASE_URL}/anime/{id}/like`,
	ANIME_DISLIKE_PATH: `${SERVER_BASE_URL}/anime/{id}/dislike`,
	ANIME_ADD_LIBRARY_PATH: `${SERVER_BASE_URL}/anime/{id}/library/add`,
	ANIME_REMOVE_LIBRARY_PATH: `${SERVER_BASE_URL}/anime/{id}/library/remove`,
	ANIME_LIBRARY_SEARCH_PATH: `${SERVER_BASE_URL}/library/anime/search`,
	ANIME_CONSTANT_PATH: `${SERVER_BASE_URL}/constant`,
	ANIME_SEARCH_PATH: `${SERVER_BASE_URL}/anime/search`,
	ANIME_BANNER_PATH: `${SERVER_BASE_URL}/anime/banner`,
	ANIME_RECENT_PATH: `${SERVER_BASE_URL}/anime/recent`,
	ANIME_POPULAR_PATH: `${SERVER_BASE_URL}/anime/popular`,
	ANIME_TRENDING_PATH: `${SERVER_BASE_URL}/anime/trending`,
	ANIME_GENRE_PATH: `${SERVER_BASE_URL}/anime/genre`,
	ANIME_RECOMMENDATION_PATH: `${SERVER_BASE_URL}/anime/{id}/recommendations`,
	ANIME_EPISODES_PATH: `${SERVER_BASE_URL}/anime/{id}/episode`,
	ANIME_EPISODE_PATH: `${SERVER_BASE_URL}/anime/{id}/episode/{episodeNumber}`,
	NEWS_PATH: `${SERVER_BASE_URL}/news`,
	NEWS_DETAILS_PATH: `${SERVER_BASE_URL}/news/{id}`,
	REQUEST_PASSWORD_RESET_PATH: `${SERVER_BASE_URL}/public/reset_password/request`,
	PUBLIC_PASSWORD_RESET_PATH: `${SERVER_BASE_URL}/public/reset_password`,
	UPDATE_PASSWORD_PATH: `${SERVER_BASE_URL}/password`,
	LIBRARY_IMPORT_PATH: `${SERVER_BASE_URL}/library/import`,
	EPISODE_PROGRESS_PATH: `${SERVER_BASE_URL}/episode/progress`
};

export const CLIENT_URLS = {
	LOGIN_URL: '/login',
	FORGOT_PASSWORD_URL: '/password/forgot',
	RESET_PASSWORD_URL: '/password/reset',
	BROWSE_URL: '/browse',
	LIBRARY_URL: '/library',
	NEWS_URL: '/news',
	ACCOUNT_URL: '/account',
	SETTINGS_URL: '/settings',
	BUG_REPORT_URL: '/bug-report',
	SEARCH_URL: '/search'
};
