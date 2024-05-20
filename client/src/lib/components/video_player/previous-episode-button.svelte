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
</script>

{#if episode && hasPrevEpisode()}
	<button class="relative flex h-full w-fit items-center space-x-2 hover:scale-125" aria-label="Previous Episode" on:click={navigate}>
		<SkipBack class="aspect-square h-full w-auto" />
		{#if isIOSDevice}
			<p class="text-sm font-extralight text-white">Episode {episode.episodeNumber - 1}</p>
		{/if}
	</button>
{/if}
