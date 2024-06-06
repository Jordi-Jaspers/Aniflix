<script lang="ts">
import { Content, Item, Root, Trigger, Value } from '$lib/components/ui/select';
import { EpisodeListCard } from '$lib/components/browse';
import type { Selected } from 'bits-ui';
import { onMount } from 'svelte';
import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { closeModal } from '$lib/api/modal-util';
import { useModalEpisodeSelector } from '$lib/components/store/localstorage';

export let anilistId: number;
export let lastSeenEpisode: number;

let isLoading: boolean = true;
let episodes: EpisodeResponse[] = [];
let totalPages: number = 0;

const pageSize = 12;

onMount(async () => {
	const response: Response = await curl(SERVER_URLS.ANIME_EPISODES_PATH.replace('{id}', anilistId.toString()), {
		method: 'GET'
	});

	if (response.ok) {
		episodes = await response.json();
		totalPages = Math.ceil(episodes.length / pageSize);
		isLoading = false;
	} else {
		closeModal();
	}
});

$: lowerBound = (page: number) => (page === 1 ? 1 : (page - 1) * pageSize + 1);
$: upperBound = (page: number) => (page * pageSize < episodes.length ? page * pageSize : episodes.length);
$: {
	if ($useModalEpisodeSelector.value === 0) {
		$useModalEpisodeSelector.value = lastSeenEpisode !== 0 ? getPageOfEpisode(lastSeenEpisode) : 1;
	}
	$useModalEpisodeSelector.label = `Episodes ${lowerBound($useModalEpisodeSelector.value)} - ${upperBound($useModalEpisodeSelector.value)}`;
}

function setCurrentPage(selectedPage: Selected<number> | undefined) {
	if (selectedPage) {
		$useModalEpisodeSelector = selectedPage;
	}
}

function getPageOfEpisode(episodeNumber: number) {
	return Math.ceil(episodeNumber / pageSize);
}
</script>

{#if isLoading}
	<div class="flex h-96 items-center justify-center">
		<div
			class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
			role="status"
		/>
	</div>
{:else if episodes.length > 0}
	<div class="items-center rounded-b-md py-4">
		<Root onSelectedChange={setCurrentPage} selected={$useModalEpisodeSelector}>
			<Trigger class="w-fit min-w-[200px] space-x-2 px-4">
				<Value placeholder="{episodes.length} Episodes" />
			</Trigger>
			<Content class="z-[1000] max-h-[25%] w-full overflow-y-auto scroll-smooth">
				{#each Array.from({ length: totalPages }, (_, i) => i + 1) as i}
					<Item class="w-full min-w-fit" value={i} label="Episodes {lowerBound(i)} - {upperBound(i)}" />
				{/each}
			</Content>
		</Root>
	</div>

	{#each episodes as episode}
		{#if episode.episodeNumber >= lowerBound($useModalEpisodeSelector.value) && episode.episodeNumber <= upperBound($useModalEpisodeSelector.value)}
			<EpisodeListCard episode={episode} />
		{/if}
	{/each}
{:else}
	<div class="flex h-96 items-center justify-center">
		<p class="text-lg font-semibold text-gray-500">No episodes found</p>
	</div>
{/if}
