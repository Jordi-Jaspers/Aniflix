<script lang="ts">
    import * as Carousel from "$lib/components/ui/carousel/index.js";
    import * as Card from "$lib/components/ui/card/index.js";
    import {ChevronRight, InfoIcon, PlayIcon, PlusCircleIcon} from "lucide-svelte";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {Skeleton} from "$lib/components/ui/skeleton";

    export let title: string;
    export let genre: string = '';
    export let url: string;

    let collection: AnimeResponse[] | EpisodeResponse[];
    onMount(async () => {
        const body: AnimeRequest = {
            page: 1,
            perPage: 25,
            title: '',
            genre: genre,
            season: ''
        };

        const response: Response = await curl(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            collection = await response.json();
        }
    });
</script>
<div class="px-[4%] lg:px-[60px] pt-8">
    <div class="py-2 flex justify-between">
        <h1 class="text-white font-bold text-lg py-1">{genre} {title}</h1>
        <button class="flex">
            See All
            <ChevronRight/>
        </button>
    </div>
    <div class="w-full">
        <Carousel.Root>
            <Carousel.Content>
                {#if collection}
                    {#each collection as anime}
                        <Carousel.Item class="basis-1/3 md:basis-1/4 lg:basis-1/5 xl:basis-1/6">
                            <Card.Root>
                                <Card.Content class="p-0 aspect-[9/14] h-full rounded-[0.75rem] overflow-hidden bg-[#1a1920] flex flex-col">
                                    <button class="h-[85%] aspect-[9/12] w-full cursor-pointer" style="background-image: url({anime.image}); background-size: cover; background-position: center">
                                        <span class="h-full w-full bg-gradient-to-b from-transparent to-black/60 flex flex-col justify-end"/>
                                    </button>
                                    <div class="flex justify-evenly items-center h-[7.5vw] md:h-[15%] py-4 md:justify-between md:px-4 ">
                                        <div class="rounded-full bg-primary/75 justify-between items-center px-2 hover:bg-primary hidden md:flex">
                                            <PlayIcon class="w-[20%] fill-white/75 hover:fill-white"/>
                                            <p class="w-[80%] text-xs font-light text-center">Watch</p>
                                        </div>
                                        <div class="flex justify-evenly md:justify-center w-full md:w-fit space-x-2">
                                            <InfoIcon class="min-h-[24px] min-w-[24px] w-[4vw] h-[4vw] md:w-[24px] md:h-[24px] hover:opacity-75 cursor-pointer"/>
                                            <PlusCircleIcon class="min-h-[24px] min-w-[24px] w-[4vw] h-[4vw] md:w-[24px] md:h-[24px] hover:opacity-75 cursor-pointer"/>
                                        </div>
                                    </div>
                                </Card.Content>
                            </Card.Root>
                        </Carousel.Item>
                    {/each}
                {:else}
                    {#each Array(20) as i}
                        <Carousel.Item class="basis-1/4 lg:basis-1/5 xl:basis-1/6">
                            <Card.Root>
                                <Card.Content class="p-0 aspect-[9/14] h-full rounded-md overflow-hidden bg-[#1a1920] flex flex-col">
                                    <Skeleton class="aspect-[9/14] w-full"/>
                                </Card.Content>
                            </Card.Root>
                        </Carousel.Item>
                    {/each}
                {/if}
            </Carousel.Content>
            <Carousel.Previous/>
            <Carousel.Next/>
        </Carousel.Root>
    </div>
</div>
