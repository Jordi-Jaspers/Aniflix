<script lang="ts">
    import {Item} from "$lib/components/ui/carousel/index.ts";
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

{#if collection}
    {#each collection as episode}
        <Item class="basis-auto">
            <EpisodeCard {episode}/>
        </Item>
    {/each}
{:else}
    {#each Array(20) as i}
        <Item class="basis-auto">
            <Skeleton class="w-64 h-full rounded-t-[0.75rem] aspect-[420/600]"/>
        </Item>
    {/each}
{/if}



