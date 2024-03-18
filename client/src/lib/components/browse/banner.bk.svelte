<script lang="ts">
    import {Badge} from "$lib/components/ui/badge/index.js";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {onMount} from "svelte";
    import {Button} from "$lib/components/ui/button";
    import {InfoIcon, PlayIcon} from "lucide-svelte";
    import SveltePlayer from "svelte-player";
    import type {RecursivePartial} from "svelte-player/dist/players/utility.types";
    import type {PlayerConfig} from "svelte-player/dist/players/types";

    let isPlaying: boolean = false;
    let opts: RecursivePartial<PlayerConfig> = {
        youtube: {
            playerVars: {
                'autoplay': 1,
                'controls': 0,
                'https': 1,
                'wmode': 'opaque',
                'mute': 1,
                'disablekb': 1,
            },
        },
    };

    let anime: AnimeResponse;
    let description: string;
    let genres: string;
    onMount(async () => {
        const body: AnimeRequest = {
            page: 1,
            perPage: 35,
            title: '',
            genre: '',
            season: ''
        };
        const response: Response = await curl(SERVER_URLS.ANIME_TRENDING_PATH, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            const collection: AnimeResponse[] = await response.json();
            anime = collection[Math.floor(Math.random() * collection.length)];

            genres = anime.genres
                .filter(genre => genre !== "UNKNOWN")
                .map(genre => genre.charAt(0) + genre.slice(1).toLowerCase())
                .join(' • ');

            let max_characters: number = 575;
            description = anime.description.replace(/\(Source:.*\)/, '');
            description = description.length > max_characters
                ? description.substring(0, max_characters)
                : description;

            let lastOpenTagIndex = description.lastIndexOf('<');
            if (lastOpenTagIndex > 0) {
                description = description.substring(0, lastOpenTagIndex);
            }
        }
    });
</script>

{#if anime}
    <div>
        <div class="relative h-[40vw] rounded-[0.75rem] overflow-hidden border-2 border-primary/75 space-x-4 hidden md:flex">
            <div class="absolute right-0 aspect-video h-full bg-gradient-to-r from-background via-transparent to-background !z-10"/>
            <img class="object-cover object-center absolute right-0 aspect-video h-full {isPlaying ? 'hidden' : ''}"
                 src={anime.cover}
                 alt="thumbnail"/>
            <div class="absolute right-0 aspect-video h-full {isPlaying ? '' : 'hidden'}">
                <SveltePlayer url="https://www.youtube.com/watch?v={anime.trailer}"
                              config={opts}
                              height="100%"
                              width="100%"
                              on:play={() => isPlaying = true}
                              on:pause={() => isPlaying = false}
                              on:ended={() => isPlaying = false}
                >
                    <div slot="play-icon"/>
                </SveltePlayer>
            </div>

            <div class="w-[40%] z-10 p-8 flex flex-col justify-end">
                <div class="h-full flex flex-col justify-center">
                    <h1 class="uppercase font-bold text- leading-none text-[1.8vw] ">{anime.title}</h1>
                    <div class="pt-4 font-extralight flex space-x-4">
                        <Badge class='{anime.status === "COMPLETE" ? "bg-green-600" : ""}'>{anime.status}</Badge>
                        <Badge class="bg-blue-500">
                            {#if anime.subbed} SUB {:else} SUB {/if}
                        </Badge>
                    </div>
                    <article class="text-[#fefefe] text-justify text-[1vw] prose py-8">
                        {@html description}
                    </article>
                </div>

                <div class="flex space-x-4">
                    <Button class="w-[50%] space-x-2 bg-transparent border border-primary/75 text-primary/75 transition hover:bg-primary/75 hover:text-white">
                        <PlayIcon/>
                        <p>Watch Now</p>
                    </Button>
                    <Button class="w-[50%] space-x-2 bg-transparent border border-white text-white transition opacity-75 hover:opacity-100 hover:text-black hover:bg-white">
                        <InfoIcon/>
                        <p>More Info</p>
                    </Button>
                </div>
            </div>
        </div>

        <div class="h-[130vw] mt-8 rounded-xl border-2 border-primary/75 overflow-hidden space-x-4 flex md:hidden">
            <div class="w-full"
                 style="background-image: url({anime.image}); background-size: cover; background-position: center;  background-repeat: no-repeat;">
                <div class="h-full w-full bg-gradient-to-b from-transparent to-black/90 flex flex-col justify-end">
                    <div class="flex flex-col justify-center text-center items-center">
                        <h1 class="text-xl sm:text-3xl uppercase">{anime.title}</h1>
                        <p class="py-4 font-extralight flex space-x-4">
                            {genres}
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
    </div>
{/if}
