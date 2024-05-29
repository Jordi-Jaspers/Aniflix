<script lang="ts">
    import {
        Fullscreen,
        MediaControls,
        NavigationSlider,
        PictureInPicture,
        PlaybackRate,
        PreviousEpisode,
        Resolution,
        VolumeSlider
    } from '$lib/components/video_player/index';
    import {onDestroy, onMount} from 'svelte';
    import ArrowLeft from 'lucide-svelte/icons/arrow-left';
    import {NextEpisode} from '$lib/components/video_player/index.js';
    import type {SveltePlayerRef} from 'svelte-player/dist/types';
    import {goto} from '$app/navigation';
    import {CLIENT_URLS} from '$lib/api/paths';
    import type {PlayerUrl} from 'svelte-player/dist/players/types';

    export let pip: boolean;
    export let playing: boolean;
    export let episode: EpisodeResponse;
    export let currentUrl: PlayerUrl;
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

<div class="fixed bottom-0 top-0 z-10 flex h-full w-full flex-col justify-between bg-black/20 px-4 py-8"
     on:mousemove={resetMouseMoveTimer}
     role="dialog"
     aria-modal="true"
     style="transition: opacity 0.5s ease; {overlayVisible ? 'opacity: 1;' : 'opacity: 0;'}"
>
    <!-- Top row functionalities  -->
    <div class="flex-grow-1 flex h-[4vw] max-h-12 min-h-6 items-center justify-between">
        <button aria-label="Back to Browse" class="h-full w-full" on:click={() => goto(CLIENT_URLS.BROWSE_URL,{invalidateAll:true})}>
            <ArrowLeft class="aspect-square h-full w-auto hover:scale-125"/>
        </button>
        <p class="flex w-full flex-col text-right">
            <span class="text-lg font-bold">{episode?.title}</span>
            <span class="text-sm font-light">{episode?.episodeTitle}</span>
        </p>
    </div>

    <!-- Bottom row functionalities  -->
    <div class="flex flex-col space-y-4">
        <NavigationSlider {playerRef} bind:played bind:duration bind:seeking bind:showVolume bind:loaded/>
        <div class="flex-grow-1 flex h-[4vw] max-h-12 min-h-6 items-center justify-between">
            <!-- Left Side  -->
            <div class="flex h-full space-x-4 md:space-x-8">
                <MediaControls {playerRef} bind:playing/>
                <VolumeSlider bind:muted bind:volume bind:showVolume/>
            </div>

            <!-- Right Side  -->
            <div class="flex h-full space-x-4 md:space-x-8">
                <PreviousEpisode bind:episode/>
                <NextEpisode bind:episode/>
                <Resolution bind:episode bind:currentUrl/>
                <PictureInPicture bind:pip/>
                <PlaybackRate bind:playbackRate/>
                <Fullscreen/>
            </div>
        </div>
    </div>
</div>
