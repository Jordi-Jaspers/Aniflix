<script lang="ts">
	import { SkipBack } from 'lucide-svelte';

	export let episode: EpisodeResponse;
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
	<button class="h-full w-full" aria-label="Previous Episode" on:click={navigate}>
		<SkipBack class="aspect-square h-full w-auto hover:scale-125" />
	</button>
{/if}
