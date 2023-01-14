const BASE_URL = process.env.NEXT_PUBLIC_CONSUMET_BASE_URL

const ConsumetApi = {
  fetchRecentEpisodes: `${BASE_URL}/meta/anilist/recent-episodes?provider=zoro`,
  fetchRandomAnime: `${BASE_URL}/meta/anilist/random-anime`,
  fetchPopularAnime: `${BASE_URL}/meta/anilist/popular?perPage=15`,
  fetchTrending: `${BASE_URL}/meta/anilist/trending?perPage=15`,
  fetchGenre: `${BASE_URL}/meta/anilist/advanced-search?perPage=15&sort=["TRENDING_DESC", "SCORE_DESC"]&genres=["{genre}"]&type=ANIME&format=TV`,

  searchAnime: `${BASE_URL}/meta/anilist/{query}`,
  fetchAnimeDetails: `${BASE_URL}/meta/anilist/info/{id}?provider=zoro`,
  fetchEpisodeLinks: `${BASE_URL}/meta/anilist/watch/{episodeId}`
}

export default ConsumetApi
