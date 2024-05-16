<script lang="ts">
    import {goto} from '$app/navigation';
    import {SkipBack} from 'lucide-svelte';

    export let episode: EpisodeResponse;

    function hasPrevEpisode(): boolean {
        return episode.episodeNumber > 1;
    }
</script>

<button
        aria-label="Next Episode"
        class="h-full w-full"
        disabled={!hasPrevEpisode()}
        on:click={() => goto('/watch/' + episode.anilistId + '/episode/' + (episode.episodeNumber - 1))}>
    <SkipBack class="aspect-square h-full w-auto hover:scale-125"/>
</button>
