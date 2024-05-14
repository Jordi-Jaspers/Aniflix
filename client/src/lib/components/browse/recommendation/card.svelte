<script lang="ts">
	import { Play, StarIcon } from 'lucide-svelte';
	import { LibraryButton, LikeButton } from '$lib/components/general';
	import { goto } from '$app/navigation';
	import { setAnime } from '$lib/components/store/anime-context-store';

	export let recommendation: RecommendationResponse;
	setAnime(recommendation);

	function getLastSeenEpisode(): number {
		if (recommendation.lastSeenEpisode === 0) {
			return 1;
		}
		return recommendation.lastSeenEpisode;
	}
</script>

<div class={'m-[0.1em] h-[100%] min-h-[18em] overflow-hidden rounded bg-card-accent shadow-lg'}>
	<div class={'relative overflow-hidden transition-opacity duration-200 hover:opacity-75'}>
		<img src={recommendation.image} alt={recommendation.title} class={'aspect-[460/650] object-cover brightness-75'} />
		<div
			class="align-center absolute bottom-0 left-0 right-0 top-0 flex h-full items-center justify-center opacity-0 hover:opacity-100 hover:transition-opacity hover:duration-200 hover:ease-in"
		>
			<button
				class="absolute h-full w-full items-center"
				on:click={() => goto('/watch/' + recommendation.anilistId + '/episode/' + getLastSeenEpisode())}
			/>
			<div class="flex flex-col items-center justify-center">
				<div
					class="flex aspect-square h-auto w-14 items-center justify-center rounded-full border-2 border-card-foreground bg-card-accent/60 p-1.5 dark:border-[gray]"
				>
					<Play />
				</div>
				{#if recommendation.lastSeenEpisode === 0}
					<p class="text-xs">You haven't seen this anime yet</p>
				{:else}
					<p class="text-xs">Continue at {recommendation.lastSeenEpisode}</p>
				{/if}
			</div>
		</div>
	</div>
	<div class="flex flex-col justify-between p-[4%]">
		<div class="h-[5.5rem] space-y-2 overflow-hidden">
			<div class="flex w-full items-center text-sm text-muted-foreground">
				<div class="flex items-center space-x-1">
					<p>{recommendation.rating / 10}</p>
					<StarIcon class="ml-0.5 h-3 w-fit fill-amber-300 text-amber-300" />
				</div>

				<span class="mx-2"> | </span>
				<span> {recommendation.episodes} Episodes </span>
				<div class="!z-40 mx-2 hidden h-4 items-center justify-center rounded border border-white/40 px-1.5 text-xs md:flex">HD</div>
			</div>
			<h4 class="text-md flex items-center font-bold">
				{recommendation.title
					.split(' ')
					.map((word) => word.charAt(0).toUpperCase() + word.slice(1))
					.join(' ')
					.split('-')
					.map((word) => word.charAt(0).toUpperCase() + word.slice(1))
					.join('-')}
			</h4>
		</div>
		<div class="my-4 flex h-10 justify-center space-x-8">
			<LikeButton />
			<LibraryButton />
		</div>
	</div>
</div>
