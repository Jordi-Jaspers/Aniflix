<script lang="ts">
	import Hls from 'hls.js';
	import { goto } from '$app/navigation';
	import { CLIENT_URLS } from '$lib/api/paths';
	import { ArrowLeft } from 'lucide-svelte';

	export let episode: EpisodeResponse;
	export let isReady: boolean;

	let currentResolution: string = 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8';

	let video: HTMLVideoElement;

	// hls.js is not supported on platforms that do not have Media Source Extensions (MSE) enabled.
	// When the browser has built-in HLS support (check using `canPlayType`), we can provide an HLS manifest (i.e. .m3u8 URL) directly to the video element through the `src` property.
	// This is using the built-in support of the plain video element, without using hls.js.
	// Note: it would be more normal to wait on the 'canplay' event below however on Safari (where you are most likely to find built-in HLS support) the video.src URL must be on the user-driven
	// white-list before a 'canplay' event will be emitted; the last video event that can be reliably listened-for when the URL is not on the white-list is 'loadedmetadata'.
	function load(src: string) {
		if (Hls.isSupported()) {
			const hls: Hls = new Hls();
			hls.loadSource(currentResolution);
			hls.attachMedia(video);
			hls.on(Hls.Events.MANIFEST_PARSED, function () {
				video.play();
			});
			isReady = true;
		} else if (video.canPlayType('application/vnd.apple.mpegurl')) {
			video.src = currentResolution;
			video.addEventListener('loadedmetadata', function () {
				video.play();
			});
			isReady = true;
		}
	}

	$: if (episode) {
		currentResolution = episode.streamingLinks.sources[0].src;
	}

	$: if (currentResolution) load(currentResolution);
</script>

<div class="flex h-screen w-screen items-center justify-center overflow-hidden">
	<div class="flex-grow-1 absolute inset-0 !z-[1000] flex h-[4vw] max-h-12 min-h-6 items-center justify-between px-4 py-8">
		<button
			aria-label="Back to Browse"
			class="relative aspect-square h-full w-auto hover:scale-125"
			on:click={() => goto(CLIENT_URLS.BROWSE_URL)}
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
</div>
