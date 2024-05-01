package org.jordijaspers.aniflix.api.consumed.consumet;

/**
 * A utility class containing constants used in the CONSUMET API.
 */
@SuppressWarnings("LineLength")
public final class ConsumetConstants {

    /**
     * Some consistent used constants.
     */
    public static final class Constants {

        public static final String ESCAPED_QUOTE = "\"";

        public static final String SLASH = "/";

        public static final String COMMA = ",";

    }

    /**
     * Configured endpoints for the CONSUMET API.
     */
    public static final class Endpoints {

        public static final String RANDOM_ANIME = "/meta/anilist/random-anime";

        public static final String RECENT_EPISODES = "/meta/anilist/recent-episodes";

        public static final String POPULAR_ANIME = "/meta/anilist/popular";

        public static final String TRENDING_ANIME = "/meta/anilist/trending";

        public static final String EPISODE_LINKS = "/meta/anilist/watch/{episodeId}";

        public static final String ANIME_DETAILS = "/meta/anilist/info/{id}";

        public static final String ANIME_DATA = "/meta/anilist/data/{id}";

        public static final String ADVANCED_SEARCH = "/meta/anilist/advanced-search";

        public static final String SEARCH = "/meta/anilist/{query}";

        public static final String ANIME_NEWS_FEED = "/news/ann/recent-feeds";

        public static final String ANIME_NEWS_DETAILS = "/news/ann/info";
    }

    /**
     * Default parameters and values used in the CONSUMET API.
     */
    public static final class QueryParams {

        // =========================== PARAM FIELDS ===========================

        public static final String QUERY_PARAM = "query";

        public static final String PAGE_PARAM = "page";

        public static final String PER_PAGE_PARAM = "perPage";

        public static final String GENRES_PARAM = "genres";

        public static final String TYPE_PARAM = "type";

        public static final String SORT_PARAM = "sort";

        public static final String FORMAT_PARAM = "format";

        public static final String SEASON_PARAM = "season";

        // =========================== DEFAULT PARAM VALUES ===========================

        public static final String ZERO_VALUE = "0";

        public static final int PAGE_VALUE = 1;

        public static final int PER_PAGE_VALUE = 25;

        public static final int PER_PAGE_VALUE_RECENT = 50;

    }
}
