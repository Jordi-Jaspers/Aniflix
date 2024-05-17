<script lang="ts">
    import SveltePlayer from 'svelte-player';
    import type {OnProgressProps, PlayerConfig, PlayerUrl} from 'svelte-player/dist/players/types';
    import type {SveltePlayerRef} from 'svelte-player/dist/types';
    import type {RecursivePartial} from "svelte-player/dist/players/utility.types";

    export let url: PlayerUrl = '';
    export let muted: boolean = false;
    export let playing: boolean = true;

    let playerRef: SveltePlayerRef;
    let prevUrl: PlayerUrl = '';
    let pip: boolean = false;
    let controls: boolean = false;
    let light: boolean = false;
    let seeking: boolean = false;
    let loop: boolean = false;
    let volume: number = 1;
    let played: number = 0;
    let loaded: number = 0;
    let duration: number = 0;
    let playbackRate: number = 1.0;

    const opts: RecursivePartial<PlayerConfig> = {
        youtube: {
            playerVars: {
                autoplay: 1,
                controls: 0,
                https: 1,
                wmode: 'opaque',
                disablekb: 1,
                muted: 0
            }
        }
    };

    function load(requestUrl: PlayerUrl) {
        prevUrl = url;
        url = requestUrl;
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

    $: onPrevURLStateChange(prevUrl);
</script>

{#if import.meta.env.VITE_ENV !== 'development'}
    <div class="brightness-85 h-full {playing ? '' : 'hidden'}">
        <SveltePlayer
                {url}
                bind:this={playerRef}
                config={opts}
                {muted}
                {playing}
                {playbackRate}
                {volume}
                {controls}
                {loop}
                {pip}
                {light}
                height="100%"
                width="100%"
                on:ready={handleReady}
                on:play={() => (playing = true)}
                on:pause={() => (playing = false)}
                on:playbackRateChange={handleOnPlaybackRateChange}
                on:ended={() => (playing = loop)}
                on:progress={handleProgress}
                on:duration={handleDuration}
        >
            <div slot="play-icon"/>
        </SveltePlayer>
    </div>
{/if}

