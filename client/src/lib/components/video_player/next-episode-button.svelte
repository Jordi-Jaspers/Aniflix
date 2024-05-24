<script lang="ts">
    import {SkipForward} from 'lucide-svelte';
    import {isIOS} from '$lib/api/util';

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

    $: showNextEpisodeButton = episode && hasNextEpisode();
</script>

{#if episode}
    <button class="relative h-full w-fit items-center space-x-2 hover:scale-125 flex {showNextEpisodeButton ? 'opacity-100' : 'opacity-0'}"
            aria-label="Next Episode" disabled={!showNextEpisodeButton} on:click={navigate}>
        {#if isIOSDevice}
            <p class="text-sm font-extralight text-white">Episode {episode.episodeNumber + 1}</p>
        {/if}
        <SkipForward class="aspect-square h-full w-auto"/>
    </button>
{/if}
