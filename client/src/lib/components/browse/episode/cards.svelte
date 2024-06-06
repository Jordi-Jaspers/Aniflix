<script lang="ts">
import { Item } from '$lib/components/ui/carousel/index';
import { onMount } from 'svelte';
import { curl } from '$lib/api/client';
import { Skeleton } from '$lib/components/ui/skeleton';
import { EpisodeCard } from '$lib/components/browse';
import { SERVER_URLS } from '$lib/api/paths';

export let isLoading = true;
let collection: EpisodeResponse[];

onMount(async () => {
	const body: AnimeRequest = {
		page: 1,
		perPage: 25,
		title: '',
		genre: '',
		season: ''
	};

	const response: Response = await curl(SERVER_URLS.ANIME_RECENT_PATH, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(body)
	});

	if (response.ok) {
		collection = await response.json();
	}
});

$: isLoading = !collection || collection.length === 0;
</script>

{#if collection}
	{#each collection as episode}
		<Item class="basis-auto">
			<EpisodeCard episode={episode} />
		</Item>
	{/each}
{:else}
	{#each Array(20) as i}
		<Item class="basis-auto">
			<Skeleton class="aspect-[420/600] h-full w-48 rounded-t-[0.75rem] md:w-56" />
		</Item>
	{/each}
{/if}
