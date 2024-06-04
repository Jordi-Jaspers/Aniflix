<script lang="ts">
import { onMount } from 'svelte';
import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { FilterBar, SearchFilter } from '$lib/components/filter/index.js';
import { PaginationBar } from '$lib/components/search/index.js';
import { AnimeCard } from '$lib/components/browse';
import { Skeleton } from '$lib/components/ui/skeleton';

let paginatedAnime: PageResponse<AnimeResponse>;

let isLoading: boolean = false;
let isRequesting: boolean = true;

let pageNumber: number = 1;
let request: LibrarySearchRequest = {
	page: 0,
	pageSize: 25,
	query: '',
	afterYear: 0,
	beforeYear: 0,
	minRating: 0,
	maxRating: 0,
	genre: [],
	status: [],
	watchStatus: []
};

async function fetchAnime() {
	isRequesting = true;
	const response: Response = await curl(SERVER_URLS.ANIME_LIBRARY_SEARCH_PATH, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(request)
	});

	if (response.ok) {
		paginatedAnime = await response.json();
		isRequesting = false;
	}
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

onMount(fetchAnime);

$: if (request) fetchAnime();
$: request.page = pageNumber - 1;
$: isLoading = isRequesting || !paginatedAnime;
</script>

<svelte:head>
	<title>Library - Aniflix</title>
	<meta name="description" content="The user saved library of Aniflix. Here you can find all your saved anime." />
	<meta name="keywords" content="Aniflix, library, anim, saved, user" />
</svelte:head>

<div class="max-w-screen mx-auto min-h-screen max-w-[1096px] overflow-hidden px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">Personal Library</h1>

	<div class="my-8 flex flex-col items-center justify-center space-y-2">
		<SearchFilter bind:query={request.query} />
		<FilterBar
			bind:afterYear={request.afterYear}
			bind:beforeYear={request.beforeYear}
			bind:genre={request.genre}
			bind:minRating={request.minRating}
			bind:maxRating={request.maxRating}
			bind:status={request.status}
			bind:watchStatus={request.watchStatus}
		/>
	</div>

	{#if !isLoading}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each paginatedAnime.content as anime}
				<div class="flex h-full flex-col overflow-hidden rounded-t-md">
					<AnimeCard bind:anime={anime} />
				</div>
			{/each}
		</div>
		<PaginationBar bind:totalElements={paginatedAnime.totalElements} bind:pageNumber={pageNumber} bind:pageSize={paginatedAnime.pageSize} />
	{:else}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each Array(10) as _}
				<div class="flex h-full flex-col overflow-hidden rounded-t-md">
					<Skeleton class="aspect-[420/600] h-full w-48 rounded-t-[0.75rem] md:w-56" />
				</div>
			{/each}
		</div>
		<PaginationBar totalElements={request.pageSize} pageNumber={request.page} pageSize={request.pageSize} />
	{/if}
</div>
