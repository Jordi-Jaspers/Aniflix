package org.jordijaspers.aniflix.api;

/**
 * Utility class containing all possible endpoints within the application.
 */
public final class Paths {

    /* ------------------------------- BASIC API CONFIGURATION ------------------------------- */

    public static final String BASE_PATH = "/api";

    public static final String PUBLIC_PATH = BASE_PATH + "/public";

    public static final String AUTH_PATH = BASE_PATH + "/auth";

    public static final String OPENAPI_PATH = "/v3/api-docs";

    public static final String ERROR_PATH = "/error";

    public static final String WILDCARD_PART = "/**";

    public static final String WILDCARD = "*";

    /* ------------------------------- SEPARATE PATH PARTS ------------------------------- */

    public static final String ID_PART = "/{id}";

    public static final String ADMIN_PART = "/admin";

    public static final String ANIME_PART = "/anime";

    public static final String SEARCH_PART = "/search";

    public static final String IMPORT_PART = "/import";

    /* ------------------------------- PUBLIC ENDPOINTS ------------------------------- */

    public static final String PUBLIC_ACTUATOR_PATH = BASE_PATH + "/actuator";

    public static final String PUBLIC_RESET_PASSWORD_PATH = PUBLIC_PATH + "/reset_password";

    public static final String PUBLIC_REQUEST_PASSWORD_RESET_PATH = PUBLIC_RESET_PASSWORD_PATH + "/request";

    /* ------------------------------- PASSWORD MANAGEMENT ENDPOINTS ------------------------------- */

    public static final String UPDATE_PASSWORD_PATH = BASE_PATH + "/password";

    /* ------------------------------- AUTH ENDPOINTS ------------------------------- */

    public static final String REGISTER_PATH = AUTH_PATH + "/register";

    public static final String USER_VALIDATION_PATH = AUTH_PATH + "/validate";

    public static final String RESEND_VALIDATION_PATH = USER_VALIDATION_PATH + "/resend";

    public static final String AUTHORIZE_PATH = AUTH_PATH + "/authorize";

    public static final String TOKEN_PATH = AUTH_PATH + "/token";

    public static final String LOGOUT_PATH = AUTH_PATH + "/logout";

    /* ------------------------------- USER ENDPOINTS ------------------------------- */

    public static final String USER_PATH = BASE_PATH + "/user";

    public static final String USER_DETAILS = USER_PATH + "/details";

    public static final String USER_UPDATE_EMAIL_PATH = USER_DETAILS + "/email";

    public static final String VALIDATE_EMAIL_PATH = USER_UPDATE_EMAIL_PATH + "/validate";

    /* ------------------------------- ANIME ENDPOINTS ------------------------------- */

    public static final String ANIME_BASE_PATH = BASE_PATH + ANIME_PART;

    public static final String ANIME_CONSTANT = BASE_PATH + "/constant";

    public static final String ANIME_SEARCH = ANIME_BASE_PATH + SEARCH_PART;

    public static final String ANIME_PATH = ANIME_BASE_PATH + ID_PART;

    public static final String ANIME_DETAILS = ANIME_BASE_PATH + ID_PART + "/details";

    public static final String ANIME_INFO = ANIME_BASE_PATH + ID_PART + "/info";

    public static final String ANIME_GENRE = ANIME_BASE_PATH + "/genre";

    public static final String ANIME_RECENT = ANIME_BASE_PATH + "/recent";

    public static final String ANIME_POPULAR = ANIME_BASE_PATH + "/popular";

    public static final String ANIME_TRENDING = ANIME_BASE_PATH + "/trending";

    public static final String ANIME_BANNER = ANIME_BASE_PATH + "/banner";

    public static final String ANIME_NEXT_EPISODE = ANIME_BASE_PATH + ID_PART + "/next_airing_episode";

    /* ------------------------------- EPISODE ENDPOINTS ------------------------------- */

    public static final String EPISODES_PATH = ANIME_PATH + "/episode";

    public static final String EPISODE_PATH = EPISODES_PATH + "/{episodeNumber}";

    /* ------------------------------- PROGRESS ENDPOINTS ------------------------------- */

    public static final String PROGRESS_PATH = BASE_PATH + "/episode/progress";

    /* ------------------------------- NEWS ENDPOINTS ------------------------------- */

    public static final String NEWS_PATH = BASE_PATH + "/news";

    public static final String NEWS_DETAILS = NEWS_PATH + ID_PART;

    /* ------------------------------- INTERACTION ENDPOINTS ------------------------------- */

    public static final String LIKE_ANIME_PATH = ANIME_PATH + "/like";

    public static final String UNLIKE_ANIME_PATH = ANIME_PATH + "/dislike";

    public static final String ADD_TO_LIBRARY_PATH = ANIME_PATH + "/library/add";

    public static final String REMOVE_FROM_LIBRARY_PATH = ANIME_PATH + "/library/remove";

    public static final String ANIME_RECOMMENDATIONS = ANIME_PATH + "/recommendations";

    /* ------------------------------- LIBRARY ENDPOINTS ------------------------------- */

    public static final String LIBRARY_PATH = BASE_PATH + "/library";

    public static final String LIBRARY_IMPORT_PATH = LIBRARY_PATH + IMPORT_PART;

    public static final String ANIME_LIBRARY_PATH = LIBRARY_PATH + ANIME_PART;

    public static final String ANIME_LIBRARY_SEARCH_PATH = ANIME_LIBRARY_PATH + SEARCH_PART;

    /* ------------------------------- END ------------------------------- */

    private Paths() {
        // private constructor to prevent instantiation.
    }
}
