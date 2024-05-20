<script lang="ts">
	import { Pause, Play, RotateCcw, RotateCw } from 'lucide-svelte';
	import type { SveltePlayerRef } from 'svelte-player/dist/types';

	export let playing: boolean;
	export let playerRef: SveltePlayerRef;

	function togglePlay() {
		playing = !playing;
	}

	function rewind() {
		playerRef.seekTo(playerRef.getCurrentTime() - 15);
	}

	function forward() {
		playerRef.seekTo(playerRef.getCurrentTime() + 15);
	}
</script>

<button aria-label="rewind" class="h-full w-full" on:click={rewind}>
	<RotateCcw class="aspect-square h-full w-auto hover:scale-125" />
</button>

<button aria-label="play" class="h-full w-full hover:scale-125" on:click={togglePlay}>
	{#if playing}
		<Pause class="aspect-square h-full w-auto fill-white stroke-[1px]" />
	{:else}
		<Play class="aspect-square h-full w-auto fill-white stroke-[1px]" />
	{/if}
</button>

<button aria-label="forward" class="h-full w-full" on:click={forward}>
	<RotateCw class="aspect-square h-full w-auto hover:scale-125" />
</button>
