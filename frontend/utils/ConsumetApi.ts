const BASE_URL = process.env.NEXT_PUBLIC_CONSUMET_BASE_URL

const ConsumetApi = {
  fetchRecentEpisodesGogoAnime: `${BASE_URL}/anime/gogoanime/recent-episodes`,
  fetchRandomAnime: `${BASE_URL}/meta/anilist/random-anime`,
  fetchPopularAnime: `${BASE_URL}/meta/anilist/popular?perPage=15`,
  fetchTrending: `${BASE_URL}/meta/anilist/trending?perPage=15`,
  fetchGenre: `${BASE_URL}/meta/anilist/advanced-search?perPage=25&type=ANIME&sort=["TRENDING_DESC", "SCORE_DESC"]&genres=["{genre}"]`,
  fetchAnimeDetails: `${BASE_URL}/meta/anilist/info/{id}?provider=gogoanime`,
  fetchEpisodeLinks: `${BASE_URL}/meta/anilist/watch/{episodeId}`,

  defaultAnimeSearch: `${BASE_URL}/meta/anilist/{query}`,
  advancedAnimeSearch: `${BASE_URL}/meta/anilist/advanced-search`,
}

export default ConsumetApi
