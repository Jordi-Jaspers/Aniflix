<script lang="ts">

    import {useShowInfoModal} from "$lib/components/store/store";
    import {goto} from "$app/navigation";
    import {Play} from "lucide-svelte";

    export let episode: EpisodeResponse;

    async function handleClickedEpisode() {
        $useShowInfoModal = false;
        await goto("/watch/" + episode.anilistId + "/" + episode.episodeNumber);
    }
</script>

{#if episode}
    <div class={"flex justify-center"} on:click={handleClickedEpisode} aria-label={"Click to watch episode"} role={"button"}>
        <div class={"border-y-[1px] border-[#404040] min-h-[6em] px-6 rounded w-full flex py-8 cursor-pointer hover:bg-[#1E1E25]"}>
            <div class={"hidden sm:block w-[5%] pr-6 text-[#d2d2d2] text-2xl min-w-fit h-fit font-poppins text-center self-center"}>
                {episode.episodeNumber}
            </div>
            <div class={"relative flex justify-center items-center overflow-hidden w-[40%] sm:w-[20%] rounded-xl"}>
                <img
                        src={episode.image}
                        alt={episode.title}
                        class={"object-cover rounded-xl w-full aspect-video"}
                        width={2000}
                        height={2000}
                />
                <div class="align-center flex h-full justify-center items-center absolute top-0 bottom-0 left-0 right-0 opacity-0 hover:transition-opacity hover:opacity-100 hover:duration-100 hover:ease-in">
                    <button class="bg-[#141414]/60 flex h-14 w-14 items-center justify-center rounded-full border-[1px] border-white">
                        <Play class="text-white h-10 w-10"/>
                    </button>
                </div>
            </div>
            <div class={"w-[60%] sm:w-[75%]"}>
                <div class={"pl-6 h-full"}>
                    <div class={"relative flex h-full flex-col justify-center sm:h-fit sm:flex-row sm:justify-between pb-2 space-x-4"}>
                        <p class={"text-xs font-semibold text-white sm:text-lg sm:font-bold font-poppins"}>
                            Episode {episode.episodeNumber}
                        </p>
                    </div>
                    <p class={"hidden sm:block text-[#d2d2d2] font-poppins font-light text-sm text-justify"}>
                        {#if episode.description}
                            {episode.description}
                        {:else}
                            No description available
                        {/if}
                    </p>
                </div>
            </div>
        </div>
    </div>
{/if}
