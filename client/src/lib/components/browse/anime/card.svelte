<script lang="ts">
    import {Content, Root} from "$lib/components/ui/card";
    import {openModal} from "$lib/api/util";
    import {PlayIcon} from "lucide-svelte";
    import {setAnime} from "$lib/components/store/anime-context-store";
    import {LikeButton} from "$lib/components/general";

    export let anime: AnimeResponse;
    setAnime(anime);
</script>

<Root>
    <Content class="p-0 aspect-[9/14] h-full rounded-[0.75rem] overflow-hidden bg-[#1a1920] flex flex-col">
        <button class="h-[85%] aspect-[9/12] w-full cursor-pointer"
                style="background-image: url({anime.image}); background-size: cover; background-position: center"
                on:click={() => openModal(anime.anilistId)}>
            <span class="h-full w-full bg-gradient-to-b from-transparent to-black/60 flex flex-col justify-end"/>
        </button>
        <div class="flex justify-evenly items-center h-[7.5vw] md:h-[15%] py-4 md:justify-between md:px-4 ">
            <div class="rounded-full bg-primary/75 justify-between items-center px-2 hover:bg-primary hidden md:flex">
                <PlayIcon class="w-[20%] fill-white/75 hover:fill-white"/>
                <p class="w-[80%] text-xs font-light text-center">Watch</p>
            </div>
            <div class="flex justify-evenly md:justify-center w-full md:w-fit space-x-2">
                    <LibraryButton isAdded={false}/>
                    <LikeButton/>
            </div>
        </div>
    </Content>
</Root>
