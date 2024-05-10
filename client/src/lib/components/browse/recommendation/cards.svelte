<script lang="ts">
	import { Skeleton } from '$lib/components/ui/skeleton';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { RecommendationCard } from '$lib/components/browse';

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
</script>

<div class="grid gap-4 py-4" style="grid-template-columns: repeat(auto-fill, minmax(200px, 1fr))">
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
