<script lang="ts">
    import {InfoButton} from "$lib/components/general";
    import {setAnilistId} from "$lib/components/store/anime-context-store";
    import {goto} from "$app/navigation";
    import {PlayIcon} from "lucide-svelte";

    let hovering: boolean = false;

    export let episode: EpisodeResponse;
    setAnilistId(episode.anilistId);
</script>

<div class="flex flex-col w-64 h-full rounded-t-[0.75rem] overflow-hidden">
    <div class="relative aspect-[420/600] w-auto h-full bg-card opacity-100 hover:opacity-75 transition-all text-white duration-200">
        <button class="absolute w-full h-full bg-gradient-to-b from-transparent lg:to-[99%] to-card justify-center items-center flex"
                on:click={() => goto("/watch/" + episode.anilistId + "/" + episode.episodeNumber)}
                on:mouseover={() => hovering = true}
                on:mouseleave={() => hovering = false}
                on:focus={() => hovering = true}
        >
            <PlayIcon class="h-10 w-10 text-transparent {hovering && 'text-white fill-white'} transition-all duration-200"/>
        </button>
        <img class="w-full h-full bg-center bg-no-repeat mb-2 cursor-pointer"
             src={episode.image}
             alt={episode.title}
             width={420}
             height={600}/>
    </div>

    <div class="flex w-full pb-4 bg-card">
        <div class="text-[#d2d2d2] h-full w-full flex px-[4%] object-center space-x-2">
            <div class="w-full max-h-full space-y-1">
                <p class="text-xs text-[gray] font-light whitespace-nowrap">
                    Episode {episode.episodeNumber}
                </p>
                <div class="flex justify-between space-x-2 items-center">
                    <h3 class="text-sm text-bold text-white h-[2.5rem] overflow-hidden">
                        {episode.title.toLowerCase().split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ')}
                    </h3>
                    <div class="flex justify-center h-6 w-6">
                        <InfoButton/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
