<script lang="ts">
import { Content, Item, Root } from '$lib/components/ui/select';
import { Select as SelectPrimitive } from 'bits-ui';
import { Proportions } from 'lucide-svelte';
import type { Selected } from 'bits-ui';
import type { PlayerUrl } from 'svelte-player/dist/players/types';
import { onMount } from 'svelte';

export let episode: EpisodeResponse;
export let currentUrl: PlayerUrl;

let currentResolution: Selected<string> | undefined;
function setResolution(selectedResolution: Selected<string> | undefined) {
	if (selectedResolution) {
		currentResolution = selectedResolution;
		currentUrl = selectedResolution.value;
	}
}

onMount(() => {
	if (episode) {
		setResolution({
			value: episode.streamingLinks.sources[0].src,
			label: episode.streamingLinks.sources[0].quality
		});
	}
});
</script>

<Root onSelectedChange={(selectedResolution) => setResolution(selectedResolution)} selected={currentResolution}>
	<SelectPrimitive.Trigger class="flex h-full items-center justify-between disabled:cursor-not-allowed disabled:opacity-50">
		<div aria-label="Select Resolution" class="h-full w-full">
			<Proportions class="aspect-square h-full w-auto hover:scale-125" />
		</div>
	</SelectPrimitive.Trigger>
	<Content class="z-[1000] max-h-[25%] !w-fit overflow-y-auto scroll-smooth">
		{#each episode.streamingLinks.sources as source}
			<Item value={source.src} label={source.quality} />
		{/each}
	</Content>
</Root>
