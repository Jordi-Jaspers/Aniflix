<script lang="ts">
	import { Duration } from '$lib/components/general';
	import type { SveltePlayerRef } from 'svelte-player/dist/types';

	export let played: number;
	export let loaded: number;
	export let duration: number;
	export let seeking: boolean;
	export let showVolume: boolean;
	export let playerRef: SveltePlayerRef;

	function handleSeekMouseDown() {
		seeking = true;
	}

	function handleSeekMouseUp() {
		seeking = false;
		playerRef.seekTo(parseFloat(String(played)));
	}
</script>

{#if !showVolume}
	<div class="group flex w-full items-center space-x-2">
		<div class="relative flex w-full items-center justify-center">
			<input
				type="range"
				min="0"
				max="1"
				step="any"
				bind:value={played}
				on:mousedown={handleSeekMouseDown}
				on:mouseup={handleSeekMouseUp}
				class="slider absolute z-10 w-full appearance-none bg-transparent"
			/>
			<div class="relative w-full">
				<div class="h-1 rounded-full bg-muted transition-all duration-150 group-hover:h-2">
					<div
						class="absolute h-1 rounded-full bg-muted-foreground transition-[height] duration-100 group-hover:h-2"
						style="width: {loaded * 100}%;"
					/>
					<div
						class="absolute h-1 rounded-full bg-primary transition-[height] duration-100 group-hover:h-2"
						style="width: {played * 100}%;"
					/>
				</div>
			</div>
		</div>
		<p>
			<Duration seconds={duration * (1 - played)} />
		</p>
	</div>
{/if}
