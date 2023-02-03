export enum PRIVATE_PATHS {
    HOME = '/',
    PROFILE = '/account',
    POPULAR = '/popular',
    RECENTLY_ADDED = '/recently-added',
    SETTINGS = '/settings',
    TRENDING = '/trending',
    WATCH = '/watch',
}

export enum PUBLIC_PATHS {
    LOGIN = '/login',
    LOGIN_OAUTH = '/login/oauth',
    REGISTER = '/register',
    FORGOT_PASSWORD = '/forgot-password',
    RESET_PASSWORD = '/reset-password',
    VERIFY_EMAIL = '/verify-email',
}

export const PUBLIC_PATHS_LIST: string[] = Object.values(PUBLIC_PATHS);
