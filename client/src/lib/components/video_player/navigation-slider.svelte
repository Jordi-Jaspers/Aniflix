<script lang="ts">
    import {Duration} from "$lib/components/general";
    import type {SveltePlayerRef} from "svelte-player/dist/types";

    export let played: number;
    export let loaded: number;
    export let duration: number;
    export let seeking: boolean;
    export let showVolume: boolean;
    export let playerRef: SveltePlayerRef;

    function handleSeekMouseDown() {
        seeking = true;
    }

    function handleSeekMouseUp() {
        seeking = false;
        playerRef.seekTo(parseFloat(String(played)));
    }
</script>
{#if !showVolume}
    <div class="w-full flex space-x-2 items-center group">
    <div class="relative w-full flex justify-center items-center">
        <input type="range" min="0" max="1" step="any" bind:value={played}
               on:mousedown={handleSeekMouseDown} on:mouseup={handleSeekMouseUp}
               class="z-10 absolute w-full appearance-none bg-transparent slider"/>
        <div class="relative w-full">
            <div class="h-1 group-hover:h-2 transition-all duration-150 bg-muted rounded-full">
                <div class="absolute h-1 group-hover:h-2 transition-[height] duration-100 rounded-full bg-muted-foreground" style="width: {loaded * 100}%;"/>
                <div class="absolute h-1 group-hover:h-2 transition-[height] duration-100 rounded-full bg-primary" style="width: {played * 100}%;"/>
            </div>
        </div>
    </div>
    <p>
        <Duration seconds={duration * (1 - played)}/>
    </p>
    </div>
{/if}

