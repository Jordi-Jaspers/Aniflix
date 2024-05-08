<script lang="ts">
    import {PauseIcon, Play, RotateCcw, RotateCw} from "lucide-svelte";
    import type {SveltePlayerRef} from "svelte-player/dist/types";

    export let playing: boolean;
    export let playerRef: SveltePlayerRef;

    function togglePlay() {
        playing = !playing;
    }
    function rewind() {
        playerRef.seekTo(playerRef.getCurrentTime() - 15);
    }

    function forward() {
        playerRef.seekTo(playerRef.getCurrentTime() + 15);
    }
</script>

<button aria-label="play" class="h-full w-full" on:click={togglePlay}>
    {#if playing}
        <PauseIcon class="aspect-square h-full w-auto fill-white hover:scale-125"/>
    {:else}
        <Play class="aspect-square h-full w-auto hover:scale-125"/>
    {/if}
</button>

<button aria-label="rewind" class="hidden h-full w-full" on:click={rewind}>
    <RotateCcw class="aspect-square h-full w-auto hover:scale-125"/>
</button>

<button aria-label="forward" class="hidden h-full w-full" on:click={forward}>
    <RotateCw class="aspect-square h-full w-auto hover:scale-125"/>
</button>
