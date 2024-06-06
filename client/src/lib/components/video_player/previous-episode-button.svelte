<script lang="ts">
import { SkipBack } from 'lucide-svelte';
import { isIOS } from '$lib/api/util';

export let episode: EpisodeResponse;

let isIOSDevice: boolean = isIOS(navigator.userAgent);

function hasPrevEpisode(): boolean {
	return episode.episodeNumber > 1;
}

function navigate() {
	if (episode) {
		window.location.href = `/watch/${episode.anilistId}/episode/${episode.episodeNumber - 1}`;
	}
}

$: showPrevEpisodeButton = episode && hasPrevEpisode();
</script>

{#if episode}
	<button
		class="relative flex h-full w-fit items-center space-x-2 hover:scale-125 {showPrevEpisodeButton ? 'opacity-100' : 'opacity-0'}"
		aria-label="Previous Episode"
		disabled={!showPrevEpisodeButton}
		on:click={navigate}
	>
		<SkipBack class="aspect-square h-full w-auto" />
		{#if isIOSDevice}
			<p class="text-sm font-extralight text-white">Episode {episode.episodeNumber - 1}</p>
		{/if}
	</button>
{/if}
