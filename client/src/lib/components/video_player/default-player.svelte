<script lang="ts">
	import { onMount } from 'svelte';
	import SveltePlayer from 'svelte-player';
	import type { OnProgressProps, PlayerUrl } from 'svelte-player/dist/players/types';
	import type { SveltePlayerRef } from 'svelte-player/dist/types';
	import type { Selected } from 'bits-ui';
	import { MediaControlOverlay } from '$lib/components/video_player';

	export let episode: EpisodeResponse;
	export let isReady: boolean;

	let playerRef: SveltePlayerRef;
	let currentResolution: Selected<string>;
	let prevUrl: PlayerUrl = '';
	let currentUrl: PlayerUrl = '';
	let pip: boolean = false;
	let playing: boolean = true;
	let controls: boolean = false;
	let light: boolean = false;
	let seeking: boolean = false;
	let muted: boolean = false;
	let loop: boolean = false;
	let volume: number = 1;
	let played: number = 0;
	let loaded: number = 0;
	let duration: number = 0;
	let playbackRate: number = 1.0;
	let showVolume: boolean = false;

	function load(requestUrl: PlayerUrl) {
		prevUrl = currentUrl;
		currentUrl = requestUrl;
		played = 0;
		loaded = 0;
		pip = false;
		muted = false;
	}

	function onPrevURLStateChange(prevUrlState: typeof prevUrl) {
		setTimeout(function () {
			if (prevUrlState !== '') {
				load(prevUrlState);
			}
		});
	}

	function handleOnPlaybackRateChange(event: CustomEvent<number>) {
		playbackRate = parseFloat(String(event.detail));
	}

	function handleReady() {
		playing = true;
		isReady = true;
	}

	// We only want to update time slider if we are not currently seeking
	function handleProgress(event: CustomEvent<OnProgressProps>) {
		const state = event.detail;
		if (!seeking && state.loaded !== undefined && state.played !== undefined) {
			loaded = state.loaded;
			played = state.played;
		}
	}

	function handleDuration(event: CustomEvent<number | null>) {
		duration = event.detail ?? 0;
	}

	$: onPrevURLStateChange(prevUrl);

	$: if (episode) {
		currentResolution = {
			value: episode.streamingLinks.sources[0].src,
			label: episode.streamingLinks.sources[0].quality
		};
	}

	$: if (currentResolution) load(currentResolution.value);
</script>

{#if episode}
	<MediaControlOverlay
		{playerRef}
		bind:episode
		bind:pip
		bind:playing
		bind:volume
		bind:muted
		bind:showVolume
		bind:played
		bind:playbackRate
		bind:loaded
		bind:duration
		bind:seeking
		bind:currentResolution
	/>

	<SveltePlayer
		url={currentUrl}
		bind:this={playerRef}
		{muted}
		{playing}
		{playbackRate}
		{volume}
		{controls}
		{loop}
		{pip}
		{light}
		on:ready={handleReady}
		on:play={() => (playing = true)}
		on:pause={() => (playing = false)}
		on:playbackRateChange={handleOnPlaybackRateChange}
		on:ended={() => (playing = loop)}
		on:progress={handleProgress}
		on:duration={handleDuration}
	/>
{/if}
