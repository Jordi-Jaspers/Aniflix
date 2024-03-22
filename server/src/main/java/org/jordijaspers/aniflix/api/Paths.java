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

    public static final String ACTUATOR_PATH = "/actuator";

    public static final String WILDCARD_PART = "/**";

    public static final String WILDCARD = "*";

    /* ------------------------------- SEPARATE PATH PARTS ------------------------------- */

    public static final String ID_PART = "/{id}";

    public static final String ADMIN_PART = "/admin";

    public static final String ANIME_PART = "/anime";

    public static final String SEARCH_PART = "/search";

    public static final String IMPORT_PART = "/import";

    /* ------------------------------- PUBLIC ENDPOINTS ------------------------------- */

    public static final String PUBLIC_METRICS_PATH = PUBLIC_PATH + "/metrics";

    public static final String PUBLIC_HEALTH_PATH = ACTUATOR_PATH + "/health";

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

    /* ------------------------------- ANIME ENDPOINTS ------------------------------- */

    public static final String ANIME_PATH = BASE_PATH + ANIME_PART;

    public static final String ANIME_CONSTANT = BASE_PATH + "/constant";

    public static final String ANIME_SEARCH = ANIME_PATH + SEARCH_PART;

    public static final String ANIME_DETAILS = ANIME_PATH + ID_PART;

    public static final String ANIME_GENRE = ANIME_PATH + "/genre";

    public static final String ANIME_RECENT = ANIME_PATH + "/recent";

    public static final String ANIME_POPULAR = ANIME_PATH + "/popular";

    public static final String ANIME_TRENDING = ANIME_PATH + "/trending";

    public static final String ANIME_BANNER = ANIME_PATH + "/banner";

    /* ------------------------------- INTERACTION ENDPOINTS ------------------------------- */

    public static final String LIKE_ANIME_PATH = ANIME_DETAILS + "/like";

    public static final String UNLIKE_ANIME_PATH = ANIME_DETAILS + "/dislike";

    public static final String LIBRARY_PATH = BASE_PATH + "/library";

    public static final String LIBRARY_IMPORT_PATH = LIBRARY_PATH + IMPORT_PART;

    public static final String ANIME_LIBRARY_PATH = LIBRARY_PATH + ANIME_PART;

    public static final String ANIME_LIBRARY_SEARCH_PATH = ANIME_LIBRARY_PATH + SEARCH_PART;



    /* ------------------------------- END ------------------------------- */

    private Paths() {
        // private constructor to prevent instantiation.
    }
}
