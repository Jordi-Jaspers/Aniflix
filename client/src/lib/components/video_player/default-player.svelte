<script lang="ts">
    import SveltePlayer from 'svelte-player';
    import type {OnProgressProps, PlayerUrl} from 'svelte-player/dist/players/types';
    import type {SveltePlayerRef} from 'svelte-player/dist/types';
    import {MediaControlOverlay} from '$lib/components/video_player';
    import {curl} from '$lib/api/client';
    import {SERVER_URLS} from '$lib/api/paths';
    import {onDestroy, onMount} from 'svelte';

    export let episode: EpisodeResponse;
    export let isReady: boolean = false;

    let interval: Timer;
    let playerRef: SveltePlayerRef;
    let prevUrl: PlayerUrl = '';
    let currentUrl: PlayerUrl = '';
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

    function savePlaybackProgress(percentageSeen: number) {
        if (episode && episode.anilistId !== 0) {
            const request: UpdateEpisodeProgressRequest = {
                anilistId: episode?.anilistId,
                episode: episode?.episodeNumber,
                lastSeen: Math.round(percentageSeen * 100)
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
    }

    // We only want to update time slider if we are not currently seeking
    function handleProgress(event: CustomEvent<OnProgressProps>) {
        const state = event.detail;
        if (!seeking && state.loaded !== undefined) {
            loaded = state.loaded;
            played = state.played;
        }
    }

    function handleDuration(event: CustomEvent<number | null>) {
        duration = event.detail ?? 0;
    }

    $: if (played) isReady = true;
    $: if (playerRef && isReady) playerRef.seekTo(parseFloat(String(episode.lastSeen / 100)));


    onMount(() => {
        interval = setInterval(() => savePlaybackProgress(played), 5000);
    });

    onDestroy(() => {
        clearInterval(interval);
    });

    $: onPrevURLStateChange(prevUrl);
    $: if (currentUrl) load(currentUrl);
</script>

{#if episode}
    <MediaControlOverlay
            {playerRef}
            bind:episode
            bind:pip
            bind:playing
            bind:volume
            bind:muted
            bind:showVolume
            bind:played
            bind:playbackRate
            bind:loaded
            bind:duration
            bind:seeking
            bind:currentUrl
    />

    <SveltePlayer
            bind:url={currentUrl}
            bind:this={playerRef}
            {muted}
            {playing}
            {playbackRate}
            {volume}
            {controls}
            {loop}
            {pip}
            {light}
            on:ready={handleReady}
            on:play={() => (playing = true)}
            on:pause={() => (playing = false)}
            on:playbackRateChange={handleOnPlaybackRateChange}
            on:ended={() => (playing = loop)}
            on:progress={handleProgress}
            on:duration={handleDuration}
    />
{/if}
