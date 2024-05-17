<script lang="ts">
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { CircleX, ScanSearch } from 'lucide-svelte';
	import { Button } from '$lib/components/ui/button/index.js';
	import { GenreFilter, RatingFilter, ReleaseYearFilter, StatusFilter, WatchStatusFilter } from '$lib/components/filter/index.js';
	import { PaginationBar } from '$lib/components/search/index.js';
	import { round } from '$lib/utils';
	import { AnimeCard } from '$lib/components/browse';

	let paginatedAnime: PageResponse<AnimeResponse>;
	let page: number = 0;
	let beforeYear: number;
	let afterYear: number;
	let minRating: number;
	let maxRating: number;
	let watchStatus: string[] = [];
	let genre: string[] = [];
	let status: string[] = [];
	let query: string;

	async function fetchAnime() {
		const request: LibrarySearchRequest = {
			page: page,
			pageSize: 25,
			query: query,
			watchStatus: watchStatus,
			genre: genre,
			status: status,
			minRating: round(minRating, 1) * 10,
			maxRating: round(maxRating, 1) * 10,
			afterYear: afterYear,
			beforeYear: beforeYear
		};

		const response: Response = await curl(SERVER_URLS.ANIME_LIBRARY_SEARCH_PATH, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(request)
		});

		if (response.ok) {
			paginatedAnime = await response.json();
		}
	}

	function removeFilter() {
		page = 0;
		query = '';
		watchStatus = [];
		genre = [];
		status = [];
		minRating = 0;
		maxRating = 0;
		afterYear = 0;
		beforeYear = 0;
	}

	onMount(fetchAnime);

	$: {
		if (
			page !== undefined ||
			query !== undefined ||
			watchStatus !== undefined ||
			genre !== undefined ||
			status !== undefined ||
			minRating !== undefined ||
			maxRating !== undefined ||
			afterYear !== undefined ||
			beforeYear !== undefined
		) {
			fetchAnime();
		}
	}
</script>

<head>
	<title>Library - Aniflix</title>
	<meta name="description" content="The user saved library of Aniflix. Here you can find all your saved anime." />
	<meta name="keywords" content="Aniflix, library, anim, saved, user" />
</head>

<div class="max-screen mx-auto max-w-[1096px] overflow-hidden px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">Personal Library</h1>

	<div class="my-8 flex flex-col items-center justify-center space-y-2">
		<div id="search" class="flex h-10 items-center justify-between rounded-md border border-input bg-transparent px-2 text-sm md:w-1/2">
			<input
				type="text"
				bind:value={query}
				placeholder="Search for anime..."
				class="h-full w-full bg-transparent text-sm focus:outline-none"
			/>
			<div class="flex space-x-2">
				<ScanSearch />
				<button class="hover:text-primary" on:click={() => (query = '')}>
					<CircleX />
				</button>
			</div>
		</div>
		<div id="filters" class="flex w-full justify-center">
			<div id="release-year-filter" class="no-scrollbar flex space-x-2 overflow-x-auto scroll-smooth">
				<Button on:click={removeFilter} variant="outline" class="border-primary">Remove Filters</Button>
				<ReleaseYearFilter bind:beforeYear bind:afterYear />
				<RatingFilter bind:minRating bind:maxRating />
				<WatchStatusFilter bind:watchStatus />
				<GenreFilter bind:genre />
				<StatusFilter bind:status />
			</div>
		</div>
	</div>

	{#if paginatedAnime}
		<div class="grid gap-2 pb-8" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each paginatedAnime.content as anime}
				<div class="flex h-full flex-col overflow-hidden rounded-t-[0.75rem]">
					<AnimeCard bind:anime />
				</div>
			{/each}
		</div>
		<PaginationBar bind:totalElements={paginatedAnime.totalElements} bind:pageNumber={page} bind:pageSize={paginatedAnime.pageSize} />
	{/if}
</div>
