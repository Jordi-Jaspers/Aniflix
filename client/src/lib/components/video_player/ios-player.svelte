<script lang="ts">
    import Hls from "hls.js";
    import toast from "svelte-french-toast";

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
        } else {
            toast.error('Your browser does not support HLS');
        }
    }

    $: if (episode) {
        currentResolution = episode.streamingLinks.sources[0].src;
    }

    $: if (currentResolution) load(currentResolution);
</script>

<video id="video" bind:this={video}>
    <track kind="captions" src="subs/subs_en.vtt" srclang="en" label="English" default>
</video>

