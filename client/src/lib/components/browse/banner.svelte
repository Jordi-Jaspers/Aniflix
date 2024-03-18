<script lang="ts">
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {onMount} from "svelte";
    import {Button} from "$lib/components/ui/button";
    import {InfoIcon, PlayIcon, StarIcon} from "lucide-svelte";
    import {Badge} from "$lib/components/ui/badge";

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
        <div class="my-4 px-[8%] h-[48vw] hidden md:flex items-center">
            <img class="h-full aspect-[460/650] rounded-[0.75rem] shadow-2xl"
                 height="650"
                 width="460"
                 src={anime.image}
                 alt="thumbnail"/>
            <div class="p-2 pl-8 flex flex-col h-fit">
                <div class="space-y-4">
                    <h1 class="uppercase font-bold leading-none text-[1.8vw]">{anime.title}</h1>
                    <div class="flex items-center text-[1.1vw] h-[1.2vw] w-[1.2]">
                        <Badge variant="outline" class='{anime.status === "COMPLETED" ? "bg-green-600" : "bg-primary"}'>{anime.status}</Badge>
                        <span class="mx-2"> • </span>
                        <Badge variant="outline" class="bg-blue-500">
                            {#if anime.subbed} SUB {:else} SUB {/if}
                        </Badge>
                        <span class="mx-2"> • </span>
                        {anime.rating / 10} <StarIcon class="ml-0.5 h-[1.2vw] w-fit text-amber-300 fill-amber-300"/>
                        <span class="mx-2"> • </span>
                        {anime.releaseYear}
                        <span class="mx-2"> • </span>
                        {anime.totalEpisodes} Episodes
                        <span class="mx-2"> • </span>
                        {genres.join(', ')}
                    </div>
                    <article class="py-2 w-[85%] text-white/75 font-extralight text-justify text-[1.2vw] prose">
                        {@html description}
                    </article>
                </div>

                <div class="flex space-x-4 py-8">
                    <Button class="pl-4 pr-1 space-x-4 rounded-full border-primary/75 text-white/75 transition bg-primary/75 hover:text-white">
                        <p>Watch Now</p>
                        <div class="rounded-full bg-secondary/50 w-12 p-1 flex justify-center">
                            <PlayIcon class="p-1 fill-white/75 hover:fill-white"/>
                        </div>
                    </Button>
                    <Button class="space-x-4 rounded-full border-secondary/75 text-white/75 transition bg-secondary/75 hover:bg-secondary hover:text-white">
                        <p>More Info</p>
                        <InfoIcon />
                    </Button>
                </div>
            </div>
        </div>

        <div class="h-[110vw] mx-[8%] mt-8 rounded-xl border border-primary/75 overflow-hidden space-x-4 flex md:hidden">
            <div class="w-full"
                 style="background-image: url({anime.image}); background-size: cover; background-position: center;  background-repeat: no-repeat;">
                <div class="h-full w-full bg-gradient-to-b from-transparent to-black/90 flex flex-col justify-end">
                    <div class="flex flex-col justify-center text-center items-center">
                        <h1 class="text-xl sm:text-3xl uppercase">{anime.title}</h1>
                        <p class="py-4 font-extralight flex space-x-4">
                            {genres.join(' • ')}
                        </p>
                    </div>

                    <div class="flex space-x-4 justify-center pb-8 px-8">
                        <Button class="w-[50%] space-x-2 border border-primary/75 text-white transition hover:bg-primary/75">
                            <PlayIcon/>
                            <p>Watch Now</p>
                        </Button>
                        <Button class="w-[50%] space-x-2 bg-black/80 border text-white transition opacity-100 hover:opacity-75 hover:bg-black/80">
                            <InfoIcon/>
                            <p>More Info</p>
                        </Button>
                    </div>
                </div>
            </div>
        </div>
{/if}
