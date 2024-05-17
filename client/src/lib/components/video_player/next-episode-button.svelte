<script lang="ts">
	import { SkipForward } from 'lucide-svelte';

	export let episode: EpisodeResponse;
	function hasNextEpisode(): boolean {
		return episode.episodeNumber < episode.totalEpisodes;
	}

	function navigate() {
		if (episode) {
			window.location.href = `/watch/${episode.anilistId}/episode/${episode.episodeNumber + 1}`;
		}
	}
</script>

{#if episode && hasNextEpisode()}
	<button class="h-full w-full" aria-label="Next Episode" disabled={!hasNextEpisode()} on:click={navigate}>
		<SkipForward class="aspect-square h-full w-auto hover:scale-125" />
	</button>
{/if}
