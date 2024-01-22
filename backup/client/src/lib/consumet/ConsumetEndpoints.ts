const BASE_URL = process.env.NEXT_PUBLIC_CONSUMET_BASE_URL
export default class ConsumetEndpoints {
    public static RANDOM_ANIME = `${BASE_URL}/meta/anilist/random-anime`;
    public static RECENT_EPISODES = `${BASE_URL}/meta/anilist/recent-episodes?provider=gogoanime&page={page}&perPage={results}`;
    public static POPULAR_ANIME = `${BASE_URL}/meta/anilist/popular?&page={page}&perPage={results}`;
    public static TRENDING_ANIME = `${BASE_URL}/meta/anilist/trending?&page={page}&perPage={results}`;
    public static ANIME_BY_GENRE = `${BASE_URL}/meta/anilist/advanced-search?sort=["SCORE_DESC", "TRENDING_DESC"]&type=ANIME&genres=["{genre}"]&page={page}&perPage={results}`;
    public static ANIME_DETAILS = `${BASE_URL}/meta/anilist/info/{id}?provider=gogoanime`;
    public static EPISODE_LINKS = `${BASE_URL}/meta/anilist/watch/{episodeId}?provider=gogoanime`;

    public static SEARCH = `${BASE_URL}/meta/anilist/{query}`;
    public static ADVANCED_SEARCH = `${BASE_URL}/meta/anilist/advanced-search`;
}
