<script lang="ts">
    import {
        Fullscreen,
        MediaControls,
        NavigationSlider,
        PictureInPicture,
        PlaybackRate,
        Resolution,
        VolumeSlider
    } from "$lib/components/video_player/index";
    import {onDestroy, onMount} from "svelte";
    import type {Selected} from "bits-ui";
    import ArrowLeft from "lucide-svelte/icons/arrow-left";
    import {NextEpisode} from "$lib/components/video_player/index.js";
    import type {SveltePlayerRef} from "svelte-player/dist/types";
    import {goto} from "$app/navigation";
    import {CLIENT_URLS} from "$lib/api/paths";

    export let pip: boolean;
    export let playing: boolean;
    export let episode: EpisodeResponse;
    export let currentResolution: Selected<string> | undefined;
    export let playerRef: SveltePlayerRef;
    export let volume: number;
    export let muted: boolean;
    export let showVolume: boolean;
    export let played: number;
    export let loaded: number;
    export let duration: number;
    export let seeking: boolean;
    export let playbackRate: number;

    let overlayVisible: boolean = true;
    let mouseMoveTimer: Timer;

    function hideOverlayAfterDelay() {
        mouseMoveTimer = setTimeout(() => {
            overlayVisible = false;
        }, 3000);
    }

    function resetMouseMoveTimer() {
        clearTimeout(mouseMoveTimer);
        overlayVisible = true;
        hideOverlayAfterDelay();
    }

    onMount(() => {
        hideOverlayAfterDelay();
    });

    onDestroy(() => {
        clearTimeout(mouseMoveTimer);
    });
</script>

<div class="fixed flex flex-col justify-between top-0 bottom-0 h-full w-full bg-black/20 px-4 py-8 z-10"
     on:mousemove={resetMouseMoveTimer} role="dialog" aria-modal="true"
     style="transition: opacity 0.5s ease; {overlayVisible ? 'opacity: 1;' : 'opacity: 0;'}">
    <!-- Top row functionalities  -->
    <div class="flex flex-grow-1 justify-between items-center min-h-4 h-[4vw] max-h-10">
        <button aria-label="Back to Browse" class="h-full w-full" on:click={() => goto(CLIENT_URLS.BROWSE_URL)}>
            <ArrowLeft class="aspect-square h-full w-auto hover:scale-125"/>
        </button>
        <p class="flex flex-col w-full text-right">
            <span class="text-lg font-bold">{episode?.title}</span>
            <span class="text-sm font-light">{episode?.episodeTitle}</span>
        </p>
    </div>

    <!-- Bottom row functionalities  -->
    <div class="flex flex-col space-y-4">
        <NavigationSlider {playerRef} bind:played bind:duration bind:seeking bind:showVolume bind:loaded/>
        <div class="flex flex-grow-1 justify-between items-center min-h-6 h-[4vw] max-h-12">
            <!-- Left Side  -->
            <div class="flex space-x-4 md:space-x-8 h-full">
                <MediaControls {playerRef} bind:playing/>
                <VolumeSlider bind:muted bind:volume bind:showVolume/>
            </div>

            <!-- Right Side  -->
            <div class="flex space-x-4 md:space-x-8 h-full">
                <NextEpisode bind:episode/>
                <Resolution bind:episode bind:currentResolution/>
                <PictureInPicture bind:pip/>
                <PlaybackRate bind:playbackRate/>
                <Fullscreen/>
            </div>
        </div>
    </div>
</div>
