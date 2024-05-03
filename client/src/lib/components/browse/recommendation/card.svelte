<script lang="ts">
    import {Play, StarIcon} from "lucide-svelte";

    export let recommendation: RecommendationResponse;

    function handleClickedRecommendation(anilistId: number) {
        console.log('clicked recommendation', anilistId);
        // TODO: PLay first episode of the anime.
    }
</script>

<div class={"m-[0.1em] min-h-[18em] h-[100%] cursor-pointer rounded bg-card-accent shadow-lg overflow-hidden"}>
    <div class={"relative overflow-hidden hover:opacity-75 transition-opacity duration-200"}>
        <img
                src={recommendation.image}
                alt={recommendation.title}
                class={"aspect-[460/650] object-cover brightness-75"}
                width={460}
                height={650}
        />
        <div class="align-center flex h-full justify-center items-center absolute top-0 bottom-0 left-0 right-0 opacity-0 hover:transition-opacity hover:opacity-100 hover:duration-200 hover:ease-in">
            <button class="flex items-center justify-center w-auto aspect-square h-14 p-1.5 rounded-full border-2 border-card-foreground dark:border-[gray] bg-card-accent/60 "
                    on:click={() => handleClickedRecommendation(recommendation.anilistId)}>
                <Play/>
            </button>
        </div>
    </div>
    <div class="flex flex-col justify-between p-[4%]">
        <div class="space-y-2 h-[5.5rem] overflow-hidden">
            <div class="flex items-center text-muted-foreground text-sm w-full">
                <div class="flex items-center space-x-1">
                    <p> {recommendation.rating / 10} </p>
                    <StarIcon class="ml-0.5 h-3 w-fit text-amber-300 fill-amber-300"/>
                </div>

                <span class="mx-2"> | </span>
                <span> {recommendation.episodes} Episodes </span>
                <div class="hidden md:flex mx-2 h-4 items-center justify-center rounded border border-white/40 px-1.5 text-xs !z-40">
                    HD
                </div>
            </div>
            <h3 class="flex text-md font-bold items-center">
                {recommendation.title.split(' ')
                    .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')
                    .split('-')
                    .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join('-')
                }
            </h3>
        </div>
        <div class="h-10 flex justify-center space-x-8 my-4">
            <!-- TODO: Make them buttons work for recommendations -->
            <!--            <LibraryButton />-->
            <!--            <LikeButton />-->
        </div>
    </div>
</div>


