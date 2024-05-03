<script lang="ts">
    import {LibraryButton} from "$lib/components/general";
    import {openModal} from "$lib/api/util";

    export let anime: AnimeResponse;
</script>

<div class="flex flex-col w-56 h-full rounded-t-[0.75rem] overflow-hidden">
    <div class="relative aspect-[420/600] w-auto h-full">
        <button class="absolute w-full h-full bg-gradient-to-b dark:from-transparent dark:lg:to-[99%] dark:to-card-accent justify-center items-center flex"
                on:click={() => openModal(anime.anilistId)}/>
        <img class="w-full h-full bg-center bg-no-repeat mb-2 cursor-pointer"
             src={anime.image}
             alt={anime.title}
             width={420}
             height={600}/>
    </div>

    <div class="flex w-full pb-4 bg-card-accent py-2">
        <div class="h-full w-full flex px-[4%] object-center space-x-2">
            <div class="w-full max-h-full space-y-1">
                <p class="text-xs text-muted-foreground font-light whitespace-nowrap">
                    {anime.totalEpisodes} Episodes - {anime.releaseYear ? anime.releaseYear : new Date().getFullYear()}
                </p>
                <div class="flex justify-between space-x-2 items-center">
                    <h4 class="text-sm h-[2.5rem] overflow-hidden">
                        {anime.title.toLowerCase().split(' ').map((s) => s.charAt(0).toUpperCase() + s.substring(1)).join(' ')}
                    </h4>
                    <div class="flex justify-center h-8 w-8">
                        <LibraryButton value={anime}/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
