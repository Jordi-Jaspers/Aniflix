<script lang="ts">
	import { goto } from '$app/navigation';
	import { CalendarDays } from 'lucide-svelte';

	export let episode: EpisodeResponse;

	async function handleClickedEpisode() {
		await goto('/watch/' + episode.anilistId + '/episode/' + episode.episodeNumber);
	}
</script>

{#if episode}
	<div class="flex justify-center" on:click={handleClickedEpisode} aria-label="Click to watch episode" role={'button'}>
		<div class="flex w-full cursor-pointer justify-center rounded-[0.75rem] px-4 py-2 hover:bg-primary/60">
			<div class="relative flex h-full w-full flex-row justify-between space-x-4">
				<div class="flex w-full items-center space-x-8">
					<p class="text-md font-semibold text-muted-foreground">
						{episode.episodeNumber}
					</p>
					<div class="w-full space-y-2">
						<h4 class="text-lg font-semibold">
							{episode.episodeTitle}
						</h4>
						<div class="flex w-full items-center space-x-2 text-justify text-sm font-light text-muted-foreground">
							<p class="text-sm font-light text-muted-foreground">
								{episode.duration / 60} min
							</p>
							<span class="text-muted-foreground">•</span>
							<CalendarDays class="h-4 w-4" />
							<p>
								{#if episode.airDate}
									{episode.airDate.toString().slice(0, 10)}
								{:else}
									TBD
								{/if}
							</p>
							{#if episode.lastSeen !== 0}
								<span class="hidden text-muted-foreground md:flex">•</span>
								<p class="hidden md:flex">
									Continue at {String(Math.floor((episode.duration * (episode.lastSeen / 100)) / 60)).padStart(2, '0')}:{String(
										Math.floor((episode.duration * (episode.lastSeen / 100)) % 60)
									).padStart(2, '0')}
								</p>
							{/if}
						</div>
						<div class="h-1 w-full bg-muted">
							<div class="h-1 w-full bg-primary" style="width: {episode.lastSeen}%" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
{/if}
