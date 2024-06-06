<script lang="ts">
import { onMount } from 'svelte';
import { curl } from '$lib/api/client';
import { page } from '$app/stores';
import { SERVER_URLS } from '$lib/api/paths';
import { PaginationBar } from '$lib/components/search';
import { AnimeCard } from '$lib/components/browse';
import { Skeleton } from '$lib/components/ui/skeleton';

let pageResponse: PageResponse<AnimeResponse>;

let isLoading: boolean = true;
let isRequesting: boolean = false;

let pageSize: number = 10 * 25;
const genre = $page.params.genre.toString();
let request: AnimeRequest = { page: 1, perPage: 25, genre: genre };

async function fetchAnime() {
	isRequesting = true;
	const response: Response = await curl(SERVER_URLS.ANIME_GENRE_PATH, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(request)
	});

	if (response.ok) {
		pageResponse = await response.json();
		isRequesting = false;
	}
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

onMount(fetchAnime);
$: if (request) fetchAnime();
$: isLoading = !pageResponse || pageResponse.content.length === 0 || isRequesting;
</script>

<svelte:head>
	<title>Trending - Aniflix</title>
	<meta name="description" content="Browse anime classified by genre on Aniflix" />
	<meta name="keywords" content="anime, genre, browse" />
</svelte:head>

<div class="max-w-screen mx-auto min-h-screen max-w-[1096px] overflow-hidden px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">{genre.charAt(0).toUpperCase() + genre.slice(1)} Anime</h1>
	{#if !isLoading}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each pageResponse.content as anime}
				<div class="flex h-full flex-col overflow-hidden rounded-t-md">
					<AnimeCard bind:anime={anime} />
				</div>
			{/each}
		</div>
		<PaginationBar bind:totalElements={pageSize} bind:pageNumber={request.page} bind:pageSize={pageResponse.pageSize} />
	{:else}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each Array(request.perPage) as _}
				<div class="flex h-full flex-col overflow-hidden rounded-t-md">
					<Skeleton class="aspect-[420/600] h-full w-48 rounded-t-[0.75rem] md:w-56" />
				</div>
			{/each}
		</div>
		<PaginationBar bind:totalElements={pageSize} bind:pageNumber={request.page} bind:pageSize={request.perPage} />
	{/if}
</div>
