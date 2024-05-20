<script lang="ts">
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { PaginationBar } from '$lib/components/search/index.js';
	import { AnimeCard } from '$lib/components/browse';
	import { Skeleton } from '$lib/components/ui/skeleton';

	let page: PageResponse<AnimeResponse>;

	let isLoading: boolean = true;
	let isRequesting: boolean = false;

	let pageSize: number = 10 * 25;
	let request: PageRequest = { page: 1, perPage: 25 };

	async function fetchAnime() {
		isRequesting = true;
		const response: Response = await curl(SERVER_URLS.ANIME_POPULAR_PATH, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(request)
		});

		if (response.ok) {
			page = await response.json();
			isRequesting = false;
		}
		window.scrollTo({ top: 0, behavior: 'smooth' });
	}

	onMount(fetchAnime);
	$: if (request) fetchAnime();
	$: isLoading = !page || page.content.length === 0 || isRequesting;
</script>

<svelte:head>
	<title>Popular - Aniflix</title>
	<meta name="description" content="Browse popular anime on Aniflix" />
	<meta name="keywords" content="anime, popular, browse" />
</svelte:head>

<div class="max-w-screen mx-auto min-h-screen max-w-[1096px] overflow-hidden px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">Popular Anime</h1>
	{#if !isLoading}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each page.content as anime}
				<div class="flex h-full flex-col overflow-hidden rounded-t-md">
					<AnimeCard bind:anime />
				</div>
			{/each}
		</div>
		<PaginationBar bind:totalElements={pageSize} bind:pageNumber={request.page} bind:pageSize={page.pageSize} />
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
