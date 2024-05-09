<script lang="ts">
    import {goto} from "$app/navigation";
    import {SkipForward} from "lucide-svelte";

    export let episode: EpisodeResponse;

    function hasNextEpisode() {
        return episode.totalEpisodes > episode.episodeNumber;
    }
</script>

{#if hasNextEpisode()}
    <button aria-label="Next Episode" class="h-full w-full"
            disabled={hasNextEpisode()}
            on:click={() => goto("/watch/" + episode.anilistId + "/episode/" + (episode.episodeNumber + 1))}>
        <SkipForward class="aspect-square h-full w-auto hover:scale-125"/>
    </button>
{/if}
