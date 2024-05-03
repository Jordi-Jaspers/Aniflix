<script lang="ts">

    import {useShowInfoModal} from "$lib/components/store/store";
    import {goto} from "$app/navigation";
    import {CalendarDays} from "lucide-svelte";

    export let episode: EpisodeResponse;

    async function handleClickedEpisode() {
        $useShowInfoModal = false;
        await goto("/watch/" + episode.anilistId + "/episode/" + episode.episodeNumber);
    }
</script>

{#if episode}
    <div class="flex justify-center" on:click={handleClickedEpisode} aria-label="Click to watch episode" role={"button"}>
        <div class="flex justify-center px-6 py-2 w-full cursor-pointer hover:bg-primary/60 rounded-[0.75rem]">
            <div class="relative flex flex-row justify-between h-full w-full space-x-4">
                <div class="flex items-center space-x-8">
                    <p class="font-semibold text-md text-muted-foreground">
                        {episode.episodeNumber}
                    </p>
                    <div>
                        <h4 class="font-semibold text-lg">
                            {episode.episodeTitle}
                        </h4>
                        <div class="flex items-center space-x-2 font-light text-sm text-muted-foreground w-full text-justify">
                            <p class="font-light text-sm text-muted-foreground">
                                {episode.duration / 60} min
                            </p>
                            <span class="text-muted-foreground">•</span>
                            <CalendarDays class="h-4 w-4"/>
                            <p class="">
                                {#if episode.airDate}
                                    {episode.airDate.toString().slice(0, 10)}
                                {:else}
                                    TBD
                                {/if}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
{/if}
