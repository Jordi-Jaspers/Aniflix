<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {SERVER_URLS} from "$lib/api/paths";
    import {page} from '$app/stores';
    import SveltePlayer from "svelte-player";
    import type {OnProgressProps, PlayerUrl} from "svelte-player/dist/players/types";
    import type {SveltePlayerRef} from "svelte-player/dist/types";
    import screenfull from 'screenfull';
    import type {Selected} from "bits-ui";
    import ArrowLeft from "lucide-svelte/icons/arrow-left";
    import {MediaControls, NavigationSlider, VolumeSlider} from "$lib/components/video_player/index.js";
    import {curl} from "$lib/api/client";
    import {goto} from "$app/navigation";
    import {Gauge, Maximize, PictureInPicture2, Proportions, SkipForward} from "lucide-svelte";
    import {Content, Item, Root} from "$lib/components/ui/select";
    import Logo from '$lib/assets/icons/aniflix-logo-large.png';
    import {Select as SelectPrimitive} from "bits-ui";

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
    let overlayVisible: boolean = true;
    let mouseMoveTimer: Timer;

    onMount(async () => {
        hideOverlayAfterDelay();

        const anilistId: string = $page.params.anilistId.toString();
        const episodeNumber: string = $page.params.episodeNumber.toString();
        const url: string = SERVER_URLS.ANIME_EPISODE_PATH.replace('{id}', anilistId).replace('{episodeNumber}', episodeNumber);

        const response: Response = await curl(url, {method: 'GET'});
        if (response.ok) {
            episode = await response.json();
        } else {
            episode = {
                anilistId: 0,
                title: 'Big Buck Bunny',
                episodeTitle: 'Official Trailer',
                episodeNumber: 1,
                episodeId: '/x36xhzz/x36xhzz.m3u8',
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

            }
        }

        currentResolution = {
            value: episode.streamingLinks.sources[0].src,
            label: episode.streamingLinks.sources[0].quality
        };

        episode.streamingLinks.sources.forEach((source: { src: string, quality: string }) => {
            if (source.src === 'default') {
                load(source.src);
                currentResolution = {
                    value: source.src,
                    label: source.quality
                };
            }
        });
    });

    function hideOverlayAfterDelay() {
        mouseMoveTimer = setTimeout(() => {
            overlayVisible = false;
        }, 3000); // 5 seconds delay
    }

    function resetMouseMoveTimer() {
        clearTimeout(mouseMoveTimer);
        overlayVisible = true;
        hideOverlayAfterDelay();
    }

    onDestroy(() => {
        clearTimeout(mouseMoveTimer);
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

    $: onPrevURLStateChange(prevUrl);

    function handleOnPlaybackRateChange(event: CustomEvent<number>) {
        playbackRate = parseFloat(String(event.detail));
    }

    function toggleFullScreen() {
        return screenfull.isFullscreen
            ? screenfull.exit()
            : screenfull.request(document.querySelector('.svelte-player') ?? undefined);
    }

    function togglePiP() {
        pip = !pip;
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

    function setResolution(selectedResolution: Selected<string> | undefined) {
        if (selectedResolution) {
            currentResolution = selectedResolution;
        }
    }

    $: if (currentResolution) load(currentResolution.value);
</script>

{#if !isReady && !episode}
    <div class="!z-100 w-full h-full flex justify-center items-center bg-black">
        <div class="flex flex-col items-center justify-center h-screen space-y-4">
            <div class="mx-auto flex w-[350px] flex-col justify-center space-y-6">
                <div class="px-16">
                    <img src={Logo} alt="AniFlix Logo" width={941} height={313}/>
                </div>
            </div>
            <p class="text-lg font-bold text-center">Loading...</p>
            <div class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-primary motion-reduce:animate-[spin_1.5s_linear_infinite]"
                    role="status"/>
        </div>
    </div>
{:else}
<div class="fixed flex flex-col justify-between top-0 bottom-0 h-full w-full bg-black/20 px-4 py-8 z-10"
     on:mousemove={resetMouseMoveTimer} role="dialog" aria-modal="true"
     style="transition: opacity 0.5s ease; {overlayVisible ? 'opacity: 1;' : 'opacity: 0;'}">
    <!-- Top row functionalities  -->
    <div class="flex flex-grow-1 justify-between items-center min-h-4 h-[4vw] max-h-10">
        <button aria-label="Back to Browse" class="h-full w-full" on:click={() => window.history.back()}>
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
                <button aria-label="Next Episode" class="h-full w-full" on:click={() => goto("/watch/" + episode.anilistId + "/episode/1")}>
                    <SkipForward class="aspect-square h-full w-auto hover:scale-125"/>
                </button>
                <Root onSelectedChange={(selectedResolution) => setResolution(selectedResolution)} selected={currentResolution}>
                    <SelectPrimitive.Trigger
                            class="flex h-full items-center justify-between disabled:cursor-not-allowed disabled:opacity-50">
                        <button aria-label="Next Episode" class="h-full w-full"
                                on:click={() => goto("/watch/" + episode.anilistId + "/episode/1")}>
                            <Proportions class="aspect-square h-full w-auto hover:scale-125"/>
                        </button>
                    </SelectPrimitive.Trigger>
                    <Content class="z-[1000] w-full max-h-[25%] overflow-y-auto scroll-smooth">
                        {#each episode.streamingLinks.sources as source}
                            <Item value={source.src} label={source.quality}/>
                        {/each}
                    </Content>
                </Root>
                <button aria-label="PiP" class="h-full w-full" on:click={togglePiP}>
                    <PictureInPicture2 class="aspect-square h-full w-auto hover:scale-125"/>
                </button>
                <button aria-label="Playback" class="h-full w-full" on:click={() => goto("/watch/" + episode.anilistId + "/episode/1")}>
                    <Gauge class="aspect-square h-full w-auto hover:scale-125"/>
                </button>
                <button aria-label="Fullscreen" class="h-full w-full" on:click={toggleFullScreen}>
                    <Maximize class="aspect-square h-full w-auto hover:scale-125"/>
                </button>
            </div>
        </div>
    </div>
</div>
{/if}

<SveltePlayer url={currentUrl}
              {muted}
              {playing}
              {playbackRate}
              {volume}
              {controls}
              {loop}
              {pip}
              {light}
              bind:this={playerRef}
              on:ready={handleReady}
              on:play={() => playing = true}
              on:pause={() => playing = false}
              on:playbackRateChange={handleOnPlaybackRateChange}
              on:ended={() => playing = loop}
              on:progress={handleProgress}
              on:duration={handleDuration}
/>



