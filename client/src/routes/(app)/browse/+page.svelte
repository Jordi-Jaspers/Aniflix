<script lang="ts">
	import { AnimeCards, Banner, Carousel, EpisodeCards } from '$lib/components/browse/index';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { getRandomValues } from '$lib/api/util';
	import { LoadingScreen } from '$lib/components/general';

	let anime: AnimeResponse;
	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_BANNER_PATH, { method: 'GET' });
		if (response.ok) {
			anime = await response.json();
		}
	});

	let genres: string[];
	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, { method: 'GET' });
		if (response.ok) {
			let constants: ConstantResponse = await response.json();
			genres = getRandomValues(constants.genres, 3);
		}
	});

	let isLoading = true;
	$: isLoading = !anime || !genres || genres.length === 0;
</script>

{#if isLoading}
	<LoadingScreen />
{:else}
	<div class="absolute left-0 right-0 !-z-10 h-full w-full bg-gradient-to-l from-background via-transparent to-background" />
	<div class="absolute left-0 right-0 !-z-10 h-[56rem] w-full bg-gradient-to-b from-transparent from-50% to-background" />
	<div
		class="absolute left-0 right-0 !-z-20 h-[56rem] w-full bg-cover bg-center bg-no-repeat opacity-[15%]"
		style="background-image: url({anime.cover});"
	/>
	<Banner {anime} />
	<Carousel title="Recent Episodes">
		<EpisodeCards />
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
{/if}
