<script lang="ts">
    import {SkipForward} from 'lucide-svelte';
    import {isIOS} from "$lib/api/util";

    export let episode: EpisodeResponse;
    let isIOSDevice: boolean = isIOS(navigator.userAgent);

    function hasNextEpisode(): boolean {
        return episode.episodeNumber < episode.totalEpisodes;
    }

    function navigate() {
        if (episode) {
            window.location.href = `/watch/${episode.anilistId}/episode/${episode.episodeNumber + 1}`;
        }
    }
</script>

{#if episode && hasNextEpisode()}
    <button class="relative h-full w-fit hover:scale-125 flex space-x-2 items-center" aria-label="Next Episode" disabled={!hasNextEpisode()}
            on:click={navigate}>
        {#if isIOSDevice}
            <p class="text-white font-extralight text-sm"> Episode {episode.episodeNumber + 1} </p>
        {/if}
        <SkipForward class="h-full w-auto aspect-square"/>
    </button>
{/if}
