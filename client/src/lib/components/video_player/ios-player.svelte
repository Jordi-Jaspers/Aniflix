<script lang="ts">
import Hls from 'hls.js';
import { goto } from '$app/navigation';
import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
import { ArrowLeft } from 'lucide-svelte';
import { NextEpisode, PreviousEpisode } from '$lib/components/video_player/index';
import { curl } from '$lib/api/client';
import { onDestroy, onMount } from 'svelte';

export let episode: EpisodeResponse;

let interval: Timer;
let video: HTMLVideoElement;
let currentResolution: string = 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8';

function load(src: string) {
	if (Hls.isSupported()) {
		const hls: Hls = new Hls();
		hls.loadSource(src);
		hls.attachMedia(video);
		hls.on(Hls.Events.MANIFEST_PARSED, function () {
			video.play();
		});
	} else if (video.canPlayType('application/vnd.apple.mpegurl')) {
		video.src = currentResolution;
		video.addEventListener('loadedmetadata', function () {
			video.play();
		});
	}
}

function savePlaybackProgress() {
	if (video && video.currentTime && episode && episode.anilistId !== 0) {
		console.log('Saving progress... video.currentTime: ', video.currentTime);
		const request: UpdateEpisodeProgressRequest = {
			anilistId: episode?.anilistId,
			episode: episode?.episodeNumber,
			lastSeen: Math.floor((video.currentTime / episode.duration) * 100)
		};

		curl(SERVER_URLS.EPISODE_PROGRESS_PATH, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(request)
		});
	}
}

onMount(() => {
	interval = setInterval(() => savePlaybackProgress(video.currentTime), 5000);
});

onDestroy(() => {
	clearInterval(interval);
});

$: if (video && episode) video.currentTime = Math.floor((episode.lastSeen / 100) * episode.duration);
$: if (episode) currentResolution = episode.streamingLinks.sources[0].src;
$: if (currentResolution) load(currentResolution);
</script>

<div class="flex h-screen w-screen items-center justify-center overflow-hidden">
	<div
		class="flex-grow-1 absolute left-0 top-[2rem] !z-[10] flex h-[4vw] max-h-12 min-h-6 w-full items-center justify-between space-x-4 px-4"
	>
		<button
			class="relative aspect-square h-full w-auto hover:scale-125"
			aria-label="Back to Browse"
			on:click={() => goto(CLIENT_URLS.BROWSE_URL, { invalidateAll: true })}
		>
			<ArrowLeft />
		</button>
		<p class="flex w-full flex-col text-right">
			<span class="text-lg font-bold">{episode?.title}</span>
			<span class="text-sm font-light">{episode?.episodeTitle}</span>
		</p>
	</div>

	<!-- svelte-ignore a11y-media-has-caption -->
	<!-- Ignoring this because the video element is not meant to have a caption -->
	<video id="video" bind:this={video} controls />

	<div class="flex-grow-1 absolute bottom-[2rem] left-0 !z-[10] flex h-6 w-full items-center justify-between space-x-4 px-4">
		<PreviousEpisode bind:episode={episode} />
		<NextEpisode bind:episode={episode} />
	</div>
</div>
