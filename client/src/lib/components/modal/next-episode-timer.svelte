<script lang="ts">
	import { onDestroy, onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { Badge } from '$lib/components/ui/badge';

	export let anilistId: number;

	interface NextAiringEpisodeResponse {
		hasNextEpisode: boolean;
		airingTime: Date;
		timeUntilAiring: number;
		episode: number;
	}

	let nextAiringEpisode: NextAiringEpisodeResponse;
	let timeUntilAiring: number;
	let timerInterval: Timer;

	function formatTime(seconds: number): string {
		const days = Math.floor(seconds / (3600 * 24));
		const hours = Math.floor((seconds % (3600 * 24)) / 3600);
		const minutes = Math.floor((seconds % 3600) / 60);
		const remainingSeconds = seconds % 60;
		return `${days}d ${hours}h ${minutes}m ${remainingSeconds}s`;
	}

	function updateTimer() {
		if (timeUntilAiring > 0) {
			timeUntilAiring -= 1;
		} else {
			clearInterval(timerInterval);
		}
	}

	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.ANIME_NEXT_AIRING_EPISODE_PATH.replace('{id}', anilistId.toString()), {
			method: 'GET'
		});

		if (response.ok) {
			nextAiringEpisode = await response.json();
			timeUntilAiring = nextAiringEpisode.timeUntilAiring;

			// Initialize the timer
			timerInterval = setInterval(updateTimer, 1000);
		}
	});

	onDestroy(() => {
		if (timerInterval) {
			clearInterval(timerInterval);
		}
	});
</script>

{#if nextAiringEpisode && nextAiringEpisode.hasNextEpisode}
	<div class="absolute left-0 top-0 !z-[1000] m-[1em]">
		{#if timeUntilAiring <= 0}
			<Badge class="bg-red-600">
				<span> Episode is live! </span>
			</Badge>
		{:else}
			<Badge class="bg-muted">
				<span class="text-muted-foreground">
					Episode {nextAiringEpisode.episode} airs in {formatTime(timeUntilAiring)}
				</span>
			</Badge>
		{/if}
	</div>
{/if}
