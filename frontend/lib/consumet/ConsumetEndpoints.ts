const BASE_URL = process.env.NEXT_PUBLIC_CONSUMET_BASE_URL
export default class ConsumetEndpoints {
    public static RANDOM_ANIME: string = `${BASE_URL}/meta/anilist/random-anime`;
    public static RECENT_EPISODES: string = `${BASE_URL}/meta/anilist/recent-episodes?provider=gogoanime&page={page}&perPage={results}`;
    public static POPULAR_ANIME: string = `${BASE_URL}/meta/anilist/popular?&page={page}&perPage={results}`;
    public static TRENDING_ANIME: string = `${BASE_URL}/meta/anilist/trending?&page={page}&perPage={results}`;
    public static ANIME_BY_GENRE: string = `${BASE_URL}/meta/anilist/advanced-search?sort=["TRENDING_DESC", "SCORE_DESC"]&type=ANIME&genres=["{genre}"]&page={page}&perPage={results}`;
    public static ANIME_DETAILS: string = `${BASE_URL}/meta/anilist/info/{id}?provider=gogoanime`;
    public static EPISODE_LINKS: string = `${BASE_URL}/meta/anilist/watch/{episodeId}?provider=gogoanime`;
    
    public static SEARCH: string = `${BASE_URL}/meta/anilist/{query}`;
    public static ADVANCED_SEARCH: string = `${BASE_URL}/meta/anilist/advanced-search`;
}
