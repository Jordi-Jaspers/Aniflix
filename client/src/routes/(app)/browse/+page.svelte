<script lang="ts">
    import {Banner} from "$lib/components/browse/index";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {AnimeCards, Carousel, EpisodeCards} from "$lib/components/browse/index.js";
    import {getRandomValues} from "$lib/api/util";

    let anime: AnimeResponse;
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.ANIME_BANNER_PATH, {method: 'GET'});
        if (response.ok) {
            anime = await response.json();
        }
    });
    SERVER_URLS.ANIME_RECENT_PATH
    let genres: string[];
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, {method: 'GET'});
        if (response.ok) {
            let constants: ConstantResponse = await response.json();
            genres = getRandomValues(constants.genres, 3)
        }
    });
</script>

{#if anime && genres && genres.length > 0}
    <div class="absolute left-0 right-0 w-full h-full bg-gradient-to-l from-background via-transparent to-background !-z-10"/>
    <div class="absolute left-0 right-0 w-full h-[40%] bg-gradient-to-b from-transparent from-50% to-background !-z-10"/>
    <div class="absolute left-0 right-0 w-full h-[40%] !-z-20 opacity-[10%] bg-no-repeat bg-center bg-cover" style="background-image: url({anime.cover});"/>
    <Banner anime={anime}/>
    <Carousel title="Recent Episodes">
        <EpisodeCards/>
    </Carousel>
    <Carousel title="Trending Anime">
        <AnimeCards url={SERVER_URLS.ANIME_TRENDING_PATH}/>
    </Carousel>
    <Carousel title="Popular Anime">
        <AnimeCards url={SERVER_URLS.ANIME_POPULAR_PATH}/>
    </Carousel>
    <Carousel genre={genres[0]} title="Anime">
        <AnimeCards genre={genres[0]} url={SERVER_URLS.ANIME_GENRE_PATH}/>
    </Carousel>
{/if}






