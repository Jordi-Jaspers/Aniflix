<script lang="ts">
	import { onMount } from 'svelte';
	import { SERVER_URLS } from '$lib/api/paths';
	import { page } from '$app/stores';
	import SveltePlayer from 'svelte-player';
	import type { OnProgressProps, PlayerUrl } from 'svelte-player/dist/players/types';
	import type { SveltePlayerRef } from 'svelte-player/dist/types';
	import type { Selected } from 'bits-ui';
	import { curl } from '$lib/api/client';
	import { LoadingScreen } from '$lib/components/general';
	import { MediaControlOverlay } from '$lib/components/video_player';
	import toast from 'svelte-french-toast';
	import Device from 'svelte-device-info';
	import { goto } from '$app/navigation';
	import Hls from 'hls.js';

	let episode: EpisodeResponse;
	let playerRef: SveltePlayerRef;
	let currentResolution: Selected<string>;
	let prevUrl: PlayerUrl = '';
	let currentUrl: PlayerUrl = '';
	let isReady: boolean = false;
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

	let isMobile: boolean = false;
	let isSafari: boolean = false;

	onMount(async () => {
		if (!Hls.isSupported()) {
			toast.error('HLS is not supported on this device');
			goto('/browse');
			return;
		}

		isMobile = Device.isMobile;
		isSafari = navigator.userAgent.includes('Safari') && !navigator.userAgent.includes('Chrome');
		toast("Loading HLS on " + isMobile ? "Mobile" : "Desktop" + " device with " + isSafari ? "Safari" : "Chrome" + " browser.");

		const anilistId: string = $page.params.anilistId.toString();
		const episodeNumber: string = $page.params.episodeNumber.toString();
		const url: string = SERVER_URLS.ANIME_EPISODE_PATH.replace('{id}', anilistId).replace('{episodeNumber}', episodeNumber);

		const response: Response = await curl(url, { method: 'GET' });
		if (response.ok) {
			episode = await response.json();
		} else {
			episode = {
				anilistId: 0,
				title: 'Big Buck Bunny',
				episodeTitle: 'Official Trailer',
				episodeNumber: 1,
				totalEpisodes: 1,
				image: 'https://via.placeholder.com/900',
				airDate: new Date(),
				duration: 695,
				streamingLinks: {
					referer: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
					sources: [
						{
							src: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
							quality: 'Default'
						}
					]
				}
			};
		}

		currentResolution = {
			value: episode.streamingLinks.sources[0].src,
			label: episode.streamingLinks.sources[0].quality
		};

		episode.streamingLinks.sources
			.filter((source: StreamingSourcesResponse) => source.quality === '1080p')
			.forEach((source: StreamingSourcesResponse) => {
				currentResolution = {
					value: source.src,
					label: source.quality
				};
			});
	});

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

	$: if (currentResolution) load(currentResolution.value);
</script>

{#if !isReady && !episode}
	<LoadingScreen />
{:else if !isMobile}
	<MediaControlOverlay
		bind:pip
		bind:playing
		bind:episode
		bind:volume
		bind:muted
		bind:showVolume
		bind:played
		bind:playbackRate
		bind:loaded
		bind:duration
		bind:seeking
		bind:currentResolution
		{playerRef}
	/>
{/if}

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
	config={{
		file: {
			forceHLS: isMobile,
			forceVideo: isMobile || isSafari,
			hlsVersion: '0.12.4',
			forceSafariHLS: isSafari
		}
	}}
	on:ready={handleReady}
	on:play={() => (playing = true)}
	on:pause={() => (playing = false)}
	on:playbackRateChange={handleOnPlaybackRateChange}
	on:ended={() => (playing = loop)}
	on:progress={handleProgress}
	on:duration={handleDuration}
/>
