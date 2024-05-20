<script lang="ts">
    import {SkipBack} from 'lucide-svelte';
    import {isIOS} from "$lib/api/util";

    export let episode: EpisodeResponse;

    let isIOSDevice: boolean = isIOS(navigator.userAgent);

    function hasPrevEpisode(): boolean {
        return episode.episodeNumber > 1;
    }

    function navigate() {
        if (episode) {
            window.location.href = `/watch/${episode.anilistId}/episode/${episode.episodeNumber - 1}`;
        }
    }
</script>

{#if episode && hasPrevEpisode()}
    <button class="relative h-full w-fit hover:scale-125 flex space-x-2 items-center" aria-label="Previous Episode" on:click={navigate}>
        <SkipBack class="h-full w-auto aspect-square"/>
        {#if isIOSDevice}
            <p class="text-white font-extralight text-sm"> Episode {episode.episodeNumber - 1} </p>
        {/if}
    </button>
{/if}
