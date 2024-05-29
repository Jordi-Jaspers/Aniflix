<script lang="ts">
	import { useSearchTerm, useShowSearchResults } from '$lib/components/store/store';
	import { curl } from '$lib/api/client';
	import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
	import { SearchResult } from '$lib/components/search/index';
	import { ChevronRight } from 'lucide-svelte';
	import { debounce } from '$lib/utils';
	import { goto } from '$app/navigation';

	let page = 1;
	let searchResults: AnimeResponse[] = [];

	// Subscribe to the search term with debounce
	const debouncedSearch = debounce(async (value: string) => {
		if (!value) {
			return;
		}

		const body: AnimeRequest = {
			page: page,
			perPage: 15,
			title: value,
			genre: null,
			season: null
		};

		const response: Response = await curl(SERVER_URLS.ANIME_SEARCH_PATH, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(body)
		});

		if (response.ok) {
			searchResults = await response.json();
		}
	}, 500); // Adjust delay as needed (e.g., 500 milliseconds)

	// Subscribe to debounced search function
	useSearchTerm.subscribe(debouncedSearch);
</script>

{#if $useShowSearchResults}
	<div class="mx-auto flex w-full max-w-[1096px] flex-col space-y-4 px-[4%] py-4">
		<div class="flex justify-start">
			<h1 class="w-full border-l-2 border-primary pl-4 text-2xl font-bold md:text-3xl">Search Results</h1>
		</div>
		<div class="grid gap-4 px-4" style="grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));">
			{#each searchResults as result}
				<SearchResult {result} />
			{/each}
		</div>
	</div>
{/if}
