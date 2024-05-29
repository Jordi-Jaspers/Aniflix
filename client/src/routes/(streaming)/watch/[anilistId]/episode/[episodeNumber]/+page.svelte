<script lang="ts">
	import { onMount } from 'svelte';
	import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
	import { page } from '$app/stores';
	import { curl } from '$lib/api/client';
	import { LoadingScreen } from '$lib/components/general';
	import toast from 'svelte-french-toast';
	import { goto } from '$app/navigation';
	import Hls from 'hls.js';
	import { isIOS } from '$lib/api/util';
	import { DefaultPlayer } from '$lib/components/video_player';
	import { IOSPlayer } from '$lib/components/video_player/index.js';

	let episode: EpisodeResponse;
	let isReady: boolean = false;
	let isLoading: boolean = true;
	let isIOSDevice = false;

	onMount(async () => {
		if (!Hls.isSupported()) {
			toast.error('HLS is not supported on this device');
			goto(CLIENT_URLS.BROWSE_URL);
			return;
		}

		isIOSDevice = isIOS(navigator.userAgent);
		const anilistId: string = $page.params.anilistId.toString();
		const episodeNumber: string = $page.params.episodeNumber.toString();
		const url: string = SERVER_URLS.ANIME_EPISODE_PATH.replace('{id}', anilistId).replace('{episodeNumber}', episodeNumber);

		const response: Response = await curl(url, { method: 'GET' });
		if (response.ok) {
			episode = await response.json();
			isLoading = false;
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
			isLoading = false;
		}
	});
</script>

{#if !isReady && !episode && isLoading}
	<LoadingScreen />
{/if}

{#if isIOSDevice}
	<IOSPlayer {episode} />
{:else}
	<DefaultPlayer {episode} bind:isReady />
{/if}
