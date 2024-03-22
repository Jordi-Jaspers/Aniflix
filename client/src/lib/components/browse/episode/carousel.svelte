<script lang="ts">
    import * as Carousel from "$lib/components/ui/carousel";
    import * as Card from "$lib/components/ui/card";
    import {ChevronRight} from "lucide-svelte";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {Skeleton} from "$lib/components/ui/skeleton";
    import {EpisodeCard} from "$lib/components/browse";
    import {SERVER_URLS} from "$lib/api/paths";

    let collection: EpisodeResponse[];
    onMount(async () => {
        const body: AnimeRequest = {
            page: 1,
            perPage: 25,
            title: '',
            genre: '',
            season: ''
        };

        const response: Response = await curl(SERVER_URLS.ANIME_RECENT_PATH, {
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
        <h1 class="text-white font-bold text-lg py-1">Recent Episodes</h1>
        <button class="flex">
            See All
            <ChevronRight/>
        </button>
    </div>
    <div class="w-full">
        <Carousel.Root class="max-w-screen">
            <Carousel.Content>
                {#if collection}
                    {#each collection as episode}
                        <Carousel.Item class="basis-1/3 md:basis-1/4 lg:basis-1/5 xl:basis-1/6">
                            <EpisodeCard {episode}/>
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
            <Carousel.Previous class="hidden md:flex absolute inset-0 top-[50%] left-[-2%] "/>
            <Carousel.Next class="hidden md:flex absolute top-[50%] right-[-2%]"/>
        </Carousel.Root>
    </div>
</div>
