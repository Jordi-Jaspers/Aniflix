<script lang="ts">
	import SveltePlayer from 'svelte-player';
	import type { PlayerUrl } from 'svelte-player/dist/players/types';
	import { onMount } from 'svelte';

	export let anime: AnimeResponse;
	export let muted: boolean;
	export let playing: boolean;

	let currentUrl: PlayerUrl = '';
	let prevUrl: PlayerUrl = '';
	let pip: boolean = false;
	let controls: boolean = false;
	let light: boolean = false;
	let loop: boolean = false;
	let volume: number = 1;
	let played: number = 0;
	let loaded: number = 0;

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

	function handleReady() {
		playing = true;
	}

	onMount(() => {
		if (anime) {
			load('https://www.youtube.com/watch?v=' + anime.trailer);
		}
	});

	$: onPrevURLStateChange(prevUrl);

	$: if (currentUrl) load(currentUrl);
</script>

{#if import.meta.env.VITE_ENV !== 'development'}
	<div class="brightness-85 h-full {playing ? '' : 'hidden'}">
		<SveltePlayer
			url={currentUrl}
			bind:muted
			bind:playing
			{volume}
			{controls}
			{loop}
			{pip}
			{light}
			height="100%"
			width="100%"
			playsinline
			on:ready={handleReady}
			on:play={() => (playing = true)}
			on:pause={() => (playing = false)}
			on:ended={() => (playing = loop)}
		>
			<div slot="play-icon" />
		</SveltePlayer>
	</div>
{/if}
