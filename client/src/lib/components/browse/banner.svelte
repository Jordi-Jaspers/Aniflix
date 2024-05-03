<script lang="ts">
    import {Button} from "$lib/components/ui/button";
    import {InfoIcon, PlayIcon, StarIcon} from "lucide-svelte";
    import {openModal} from "$lib/api/util";

    export let anime: AnimeResponse;
    let genres: string[] = anime.genres
        .filter(genre => genre !== "UNKNOWN")
        .slice(0, 3)
        .map(genre => genre.charAt(0) + genre.slice(1).toLowerCase());

    let max_characters: number = 575;
    let description: string = anime.description.replace(/\(Source:.*\)/, '');
    description = description.length > max_characters
        ? description.substring(0, max_characters)
        : description;

    let lastOpenTagIndex = description.lastIndexOf('<');
    if (lastOpenTagIndex > 0) {
        description = description.substring(0, lastOpenTagIndex);
    }
</script>

{#if anime}
    <div class="my-4 px-[4%] h-[48vw] max-h-[650px] mx-[4%] w-80% hidden md:flex items-center">
        <img class="h-full w-auto max-w-[390px] max-h-[550px] aspect-[460/650] rounded-[0.75rem] shadow-2xl"
             height="650"
             width="460"
             src={anime.image}
             alt="thumbnail"/>
        <div class="p-2 pl-8 flex flex-col h-fit">
            <div class="space-y-4">
                <h1 class="flex items-center uppercase font-bold leading-none text-2xl xl:text-3xl">{anime.title}</h1>
                <div class="flex items-center text-xs xl:text-sm text-muted-foreground">
                    {anime.rating / 10}
                    <StarIcon class="ml-[0.5rem] h-4 w-fit text-amber-300 fill-amber-300"/>
                    <span class="mx-2"> • </span>
                    {anime.totalEpisodes} Episodes
                    <span class="mx-2"> • </span>
                    {genres.join(', ')}
                </div>
                <article class="text-[1.4vw] xl:text-[1.2rem] py-2 opacity-75 font-extralight text-justify w-[80%] prose">
                    {@html description}
                </article>
            </div>

            <div class="flex space-x-4 py-8">
                <Button class="pl-4 pr-1 space-x-4 rounded-full opacity-80 transition hover:opacity-100">
                    <p>Watch Now</p>
                    <div class="rounded-full bg-secondary/50 w-12 p-1 flex justify-center">
                        <PlayIcon class="p-1 fill-white"/>
                    </div>
                </Button>
                <button class="flex items-center space-x-2 px-4 rounded-full opacity-75 hover:opacity-100 shadow-2xl transition bg-muted dark:border-none border-2 border-foreground"
                        on:click={() => openModal(anime.anilistId)}>
                    <span>More Info</span>
                    <InfoIcon/>
                </button>
            </div>
        </div>
    </div>
    <div class="aspect-[460/650] max-h-[650px] max-w-[460px] w-[75%] mx-auto mt-8 rounded-xl border border-primary/75 overflow-hidden space-x-4 flex md:hidden">
        <div class="w-full"
             style="background-image: url({anime.image}); background-size: cover; background-position: center;  background-repeat: no-repeat;">
            <div class="h-full w-full bg-gradient-to-b from-transparent to-black/90 flex flex-col justify-end">
                <div class="flex flex-col justify-center text-center items-center px-8">
                    <h1 class="xs:text-xs sm:text-lg uppercase font-bold">{anime.title}</h1>
                    <p class="opacity-75 text-xs font-extralight flex space-x-4 pb-4 pt-2 ">
                        {genres.join(' • ')}
                    </p>
                </div>

                <div class="flex space-x-4 justify-center pb-8 px-4">
                    <Button class="w-[50%] space-x-2 border border-primary/75 transition hover:bg-primary/75 text-sm h-8 py-4">
                        <PlayIcon class="h-4 w-4"/>
                        <p>Watch</p>
                    </Button>
                    <Button class="w-[50%] space-x-2 bg-black/80 border transition opacity-100 hover:opacity-75 hover:bg-black/80 h-8 py-4"
                            on:click={() => openModal(anime.anilistId)}>
                        <InfoIcon class="h-4 w-4"/>
                        <p>More Info</p>
                    </Button>
                </div>
            </div>
        </div>
    </div>
{/if}
