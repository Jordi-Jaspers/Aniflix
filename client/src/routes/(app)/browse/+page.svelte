<script lang="ts">
	import { AnimeCards, Banner, Carousel, EpisodeCards } from '$lib/components/browse/index';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { getRandomValues } from '$lib/api/util';
	import { LoadingScreen } from '$lib/components/general';

	let genres: string[];
	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, { method: 'GET' });
		if (response.ok) {
			let constants: ConstantResponse = await response.json();
			genres = getRandomValues(constants.genres, 3);
		}
	});

	let isLoading = true;
	let areEpisodesLoading = true;
	$: isLoading = areEpisodesLoading;
</script>

<head>
	<title>Browse - Aniflix</title>
	<meta name="description" content="Browse through the latest anime episodes and trending anime on Aniflix." />
	<meta name="keywords" content="anime, browse, episodes, trending, popular, genres" />
</head>

{#if isLoading}
	<LoadingScreen />
{/if}

{#if genres && genres.length > 0}
	<div class="absolute left-0 right-0 !-z-10 h-full w-full bg-gradient-to-l from-background via-transparent to-background" />
	<div class="absolute left-0 right-0 !-z-10 h-[56rem] w-full bg-gradient-to-b from-transparent from-50% to-background" />

	<Banner />
	<Carousel title="Recent Episodes">
		<EpisodeCards bind:isLoading={areEpisodesLoading} />
	</Carousel>
	<Carousel title="Trending Anime">
		<AnimeCards url={SERVER_URLS.ANIME_TRENDING_PATH} />
	</Carousel>
	<Carousel title="Popular Anime">
		<AnimeCards url={SERVER_URLS.ANIME_POPULAR_PATH} />
	</Carousel>
	<Carousel genre={genres[0]} title="Anime">
		<AnimeCards genre={genres[0]} url={SERVER_URLS.ANIME_GENRE_PATH} />
	</Carousel>
	<Carousel genre={genres[1]} title="Anime">
		<AnimeCards genre={genres[0]} url={SERVER_URLS.ANIME_GENRE_PATH} />
	</Carousel>
{/if}
