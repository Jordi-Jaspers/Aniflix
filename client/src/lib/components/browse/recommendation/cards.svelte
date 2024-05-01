<script lang="ts">
    import {Skeleton} from "$lib/components/ui/skeleton";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {LibraryButton, LikeButton} from "$lib/components/general";
    import {Play, StarIcon} from "lucide-svelte";

    export let anime: AnimeResponse;
    let recommendations: RecommendationResponse[];
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.ANIME_RECOMMENDATION_PATH.replace('{id}', anime.anilistId.toString()), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            recommendations = await response.json();
        }
    });

    function handleClickedRecommendation(anilistId:number) {
        console.log('clicked');
        // TODO: PLay first episode of the anime.
    }

</script>

<div class="grid gap-4" style="grid-template-columns: repeat(auto-fill, minmax(200px, 1fr))">
    {#if recommendations}
        {#each recommendations as recommendation}
                <div class={"m-[0.1em] min-h-[18em] h-[100%] cursor-pointer rounded bg-card shadow-lg border border-[#1E1E25] overflow-hidden"}>
                    <div class={"relative overflow-hidden hover:opacity-75 transition-opacity duration-200"}>
                        <img
                                src={recommendation.image}
                                alt={recommendation.title}
                                class={"aspect-[460/650] object-cover brightness-75"}
                                width={460}
                                height={650}
                        />
                        <div class="align-center flex h-full justify-center items-center absolute top-0 bottom-0 left-0 right-0 opacity-0 hover:transition-opacity hover:opacity-100 hover:duration-200 hover:ease-in">
                            <button class="bg-[#141414]/60 flex h-14 w-14 items-center justify-center rounded-full border-[1px] border-white"
                            on:click={() => handleClickedRecommendation(recommendation.anilistId)}>
                                <Play />
                            </button>
                        </div>
                    </div>
                    <div class="flex flex-col justify-between p-[4%]">
                        <div class="space-y-2 h-24 overflow-hidden">
                            <div class="flex items-center text-[gray] text-sm w-full">
                                <div class="flex items-center space-x-1">
                                    <p> {recommendation.rating / 10} </p>
                                    <StarIcon class="ml-0.5 h-3 w-fit text-amber-300 fill-amber-300"/>
                                </div>

                                <span class="mx-2"> | </span>
                                <span> {recommendation.episodes} Episodes </span>
                                <div class="hidden md:block mx-2 flex text-white h-4 items-center justify-center rounded border border-white/40 px-1.5 text-xs !z-40">
                                    HD
                                </div>
                            </div>
                            <h3 class="flex text-md font-bold text-white items-center">
                                {recommendation.title.split(' ')
                                    .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')
                                    .split('-')
                                    .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join('-')
                                }
                            </h3>
                        </div>
                        <div class="h-10 flex justify-center space-x-8">
                            <!-- TODO: Make them buttons work for recommendations -->
                            <LibraryButton value={anime}/>
                            <LikeButton value={anime}/>
                        </div>
                    </div>
                </div>
        {/each}
    {:else}
        {#each Array(25) as _}
            <Skeleton/>
        {/each}
    {/if}
</div>




