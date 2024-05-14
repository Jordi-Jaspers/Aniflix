<script lang="ts">
	import { Skeleton } from '$lib/components/ui/skeleton';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { RecommendationCard } from '$lib/components/browse';
	import { writable } from 'svelte/store';

	export let anime: AnimeResponse;
	let recommendations: RecommendationResponse[];
	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_RECOMMENDATION_PATH.replace('{id}', anime.anilistId.toString()), {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});

		if (response.ok) {
			recommendations = await response.json();
		}
	});

	const width = writable(window.innerWidth > 400 ? '200px' : '100px');
	onMount(() => {
		const updateWidth = () => {
			width.set(window.innerWidth > 400 ? '200px' : '100px');
		};

		window.addEventListener('resize', updateWidth);
		return () => {
			window.removeEventListener('resize', updateWidth);
		};
	});

	$: console.log(width);
</script>

<div class="grid gap-4 py-4" style="grid-template-columns: repeat(auto-fill, minmax({$width}, 1fr))">
	{#if recommendations}
		{#each recommendations as recommendation}
			<RecommendationCard {recommendation} />
		{/each}
	{:else}
		{#each Array(25) as _}
			<Skeleton />
		{/each}
	{/if}
</div>
