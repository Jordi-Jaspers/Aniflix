<script lang="ts">
	import { Button } from '$lib/components/ui/button/index.js';
	import { GenreFilter, RatingFilter, ReleaseYearFilter, StatusFilter, WatchStatusFilter } from '$lib/components/filter/index';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';

	export let beforeYear = 0;
	export let afterYear = 0;
	export let minRating = 0;
	export let maxRating = 0;
	export let watchStatus: string[] = [];
	export let genre: string[] = [];
	export let status: string[] = [];

	function removeFilter() {
		watchStatus = [];
		genre = [];
		status = [];
		minRating = 0;
		maxRating = 0;
		afterYear = 0;
		beforeYear = 0;
	}

	let possibleStatus: string[] = [];
	let possibleWatchStatus: string[] = [];
	let possibleGenres: string[] = [];

	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, { method: 'GET' });
		if (response.ok) {
			const constants: ConstantResponse = await response.json();
			({ status: possibleStatus, watchStatus: possibleWatchStatus, genres: possibleGenres } = constants);
		}
	});
</script>

<div id="filters" class="flex w-full justify-center">
	<div class="no-scrollbar flex space-x-2 overflow-x-auto scroll-smooth">
		<Button on:click={removeFilter} variant="outline" class="border-primary">Remove Filters</Button>
		<ReleaseYearFilter bind:beforeYear bind:afterYear />
		<RatingFilter bind:minRating bind:maxRating />
		<WatchStatusFilter bind:watchStatus {possibleWatchStatus} />
		<GenreFilter bind:genre {possibleGenres} />
		<StatusFilter bind:status {possibleStatus} />
	</div>
</div>
