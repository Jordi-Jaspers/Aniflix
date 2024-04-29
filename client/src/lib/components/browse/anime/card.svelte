<script lang="ts">
    import {setAnime} from "$lib/components/store/anime-context-store";
    import {LibraryButton} from "$lib/components/general";
    import {openModal} from "$lib/api/util";

    export let anime: AnimeResponse;
</script>

<div class="flex flex-col w-64 h-full rounded-t-[0.75rem] overflow-hidden">
    <div class="relative aspect-[420/600] w-auto h-full">
        <button class="absolute w-full h-full bg-gradient-to-b from-transparent lg:to-[99%] to-[#1a1920]"
                on:click={() => openModal(anime.anilistId)}/>
        <img class="w-full h-full bg-center bg-no-repeat mb-2 cursor-pointer"
             src={anime.image}
             alt={anime.title}
             width={420}
             height={600}/>
    </div>

    <div class="flex w-full pb-4 bg-[#1a1920]">
        <div class="text-[#d2d2d2] h-full w-full flex px-[4%] object-center space-x-2">
            <div class="w-full max-h-full space-y-1">
                <p class="text-xs text-[#666666] font-light whitespace-nowrap">
                    {anime.totalEpisodes} Episodes - {anime.releaseYear ? anime.releaseYear : new Date().getFullYear()}
                </p>
                <div class="flex justify-between space-x-2 items-center">
                    <h3 class="text-sm text-bold text-white h-[2.5rem] overflow-hidden">
                        {anime.title.toLowerCase().split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ')}
                    </h3>
                    <div class="flex justify-center h-8 w-8">
                        <LibraryButton value={anime}/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
