<script lang="ts">
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {page} from '$app/stores';
    import SveltePlayer from "svelte-player";
    import type {OnProgressProps, PlayerUrl} from "svelte-player/dist/players/types";
    import type {SveltePlayerRef} from "svelte-player/dist/types";
    import screenfull from 'screenfull';
    import {Duration} from "$lib/components/general";
    import {Content, Item, Root, Trigger, Value} from "$lib/components/ui/select";
    import type {Selected} from "bits-ui";


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
    let urlInput: string = '';

    onMount(async () => {
        const anilistId: string = $page.params.anilistId.toString();
        const episodeNumber: string = $page.params.episodeNumber.toString();
        const url: string = SERVER_URLS.ANIME_EPISODE_PATH.replace('{id}', anilistId).replace('{episodeNumber}', episodeNumber);

        const response: Response = await curl(url, {method: 'GET'});
        if (response.ok) {
            episode = await response.json();

            currentResolution = {
                value: episode.streamingLinks.sources[0].src,
                label: episode.streamingLinks.sources[0].quality
            };

            episode.streamingLinks.sources.forEach((source) => {
                if (source.src === 'default') {
                    load(source.src);
                    currentResolution = {
                        value: source.src,
                        label: source.quality
                    };
                }
            });

        }
    });

    function load(requestUrl: PlayerUrl) {
        prevUrl = currentUrl;
        currentUrl = requestUrl;
        played = 0;
        loaded = 0;
        pip = false;
        muted = true;

        console.log('loading new url: ', requestUrl);
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

    function togglePlay() {
        playing = !playing;
    }

    function toggleFullScreen() {
        return screenfull.isFullscreen
            ? screenfull.exit()
            : screenfull.request(document.querySelector('.svelte-player') ?? undefined);
    }

    function togglePiP() {
        pip = !pip;
    }

    function toggleMute() {
        muted = !muted;
    }

    function handleSeekMouseDown() {
        seeking = true;
    }

    function handleSeekMouseUp() {
        seeking = false;
        playerRef.seekTo(parseFloat(String(played)));
    }

    function handleReady() {
        playing = true;
        isReady = true;
    }

    function handleProgress(event: CustomEvent<OnProgressProps>) {
        const state = event.detail;
        // console.log('onProgress', state);
        // We only want to update time slider if we are not currently seeking
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

    $: if(currentResolution) load(currentResolution.value);
</script>

{#if !isReady}
    <div class="absolute w-full h-full flex justify-center items-center bg-black">
        <p>Loading...</p>
    </div>
{/if}
<div>
    <!--    <pre>{JSON.stringify(episode, null, 2)}</pre>-->
    <button on:click={togglePlay}>
        {playing ? 'Pause' : 'Play'}
    </button>

    <button on:click={toggleFullScreen}>Fullscreen</button>

    <div class="flex">
        <p>volume: {Math.ceil(volume * 100)}%</p>
        <input type="range" min={0} max={1} step="any" bind:value={volume} />
    </div>

    <button on:click={toggleMute}>
        {muted ? 'unmute' : 'mute'}
    </button>

    <button on:click={togglePiP}>
        {pip ? 'Disable PiP' : 'Enable PiP'}
    </button>

    <div>
        <p>Played </p>
        <input
                type="range"
                min={0}
                max={1}
                step="any"
                bind:value={played}
                on:mousedown={handleSeekMouseDown}
                on:mouseup={handleSeekMouseUp}
        />

        <p>Remaining: <Duration seconds={duration * (1 - played)} /></p>
    </div>
    <div>
        <p>Loaded</p>
        <input disabled type="range" min={0} max={1} step="any" bind:value={loaded} />
    </div>

    <Root onSelectedChange={(selectedResolution) => setResolution(selectedResolution)} selected={currentResolution}>
        <Trigger class="min-w-[25%] w-fit space-x-2">
            <Value placeholder="Select resolution"/>
        </Trigger>
        <Content class="z-[1000] w-full max-h-[25%] overflow-y-auto scroll-smooth">
            {#each episode.streamingLinks.sources as source}
                <Item value={source.src} label={source.quality}/>
            {/each}
        </Content>
    </Root>

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
</div>
