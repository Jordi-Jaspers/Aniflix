<script lang="ts">
	import { Content, Item, Root } from '$lib/components/ui/select';
	import { Proportions } from 'lucide-svelte';
	import type { Selected } from 'bits-ui';
	import { Select as SelectPrimitive } from 'bits-ui';
	import { goto } from '$app/navigation';

	export let episode: EpisodeResponse;
	export let currentResolution: Selected<string> | undefined;

	function setResolution(selectedResolution: Selected<string> | undefined) {
		if (selectedResolution) {
			currentResolution = selectedResolution;
		}
	}
</script>

<Root onSelectedChange={(selectedResolution) => setResolution(selectedResolution)} selected={currentResolution}>
	<SelectPrimitive.Trigger class="flex h-full items-center justify-between disabled:cursor-not-allowed disabled:opacity-50">
		<button aria-label="Next Episode" class="h-full w-full" on:click={() => goto('/watch/' + episode.anilistId + '/episode/1')}>
			<Proportions class="aspect-square h-full w-auto hover:scale-125" />
		</button>
	</SelectPrimitive.Trigger>
	<Content class="z-[1000] max-h-[25%] !w-fit overflow-y-auto scroll-smooth">
		{#each episode.streamingLinks.sources as source}
			<Item value={source.src} label={source.quality} />
		{/each}
	</Content>
</Root>
