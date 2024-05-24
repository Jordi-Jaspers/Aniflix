<script lang="ts">
    import SveltePlayer from 'svelte-player';
    import type {PlayerUrl} from 'svelte-player/dist/players/types';
    import {onMount} from 'svelte';
    import {isIOS} from "$lib/api/util";
    import Device from "svelte-device-info";

    export let anime: AnimeResponse;
    export let muted: boolean;
    export let playing: boolean;

    let isIOSDevice: boolean = isIOS(navigator.userAgent);
    let currentUrl: PlayerUrl = '';
    let prevUrl: PlayerUrl = '';
    let controls: boolean = false;
    let light: boolean = false;
    let loop: boolean = false;
    let volume: number = 1;
    let played: number = 0;
    let loaded: number = 0;

    function load(requestUrl: PlayerUrl) {
        prevUrl = currentUrl;
        currentUrl = requestUrl;
        volume = 1;
        played = 0;
        loaded = 0;
        muted = false;
    }

    function onPrevURLStateChange(prevUrlState: typeof prevUrl) {
        setTimeout(function () {
            if (prevUrlState !== '') {
                load(prevUrlState);
            }
        });
    }

    function handleReady() {
        playing = true;
    }

    function handlePlay() {
        playing = true;
        load('https://www.youtube.com/watch?v=' + anime.trailer);
    }

    onMount(() => {
        if (anime) {
            load('https://www.youtube.com/watch?v=' + anime.trailer);
        }
    });

    $: onPrevURLStateChange(prevUrl);
    $: if (currentUrl) load(currentUrl);
</script>

{#if import.meta.env.VITE_ENV !== 'development' && !isIOSDevice && !Device.isPhone}
    <div class="brightness-85 h-full {playing ? '' : 'hidden'}">
        <SveltePlayer
                url={currentUrl}
                {volume}
                {controls}
                {loop}
                {light}
                bind:muted
                bind:playing
                pip={false}
                height="100%"
                width="100%"
                playsinline
                on:ready={handleReady}
                on:play={handlePlay}
                on:pause={() => (playing = false)}
                on:ended={() => (playing = loop)}
        >
            <div slot="play-icon"/>
        </SveltePlayer>
    </div>
{/if}
