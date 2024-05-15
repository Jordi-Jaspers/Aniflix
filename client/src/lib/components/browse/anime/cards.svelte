<script lang="ts">
    import {Item} from '$lib/components/ui/carousel/index.ts';
    import {onMount} from 'svelte';
    import {curl} from '$lib/api/client';
    import {Skeleton} from '$lib/components/ui/skeleton';
    import {AnimeCard} from '$lib/components/browse';

    export let genre: string = '';
    export let url: string;
    export let isLoading = true;

    let collection: AnimeResponse[];
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

    $: isLoading = !collection || collection.length === 0;
</script>

{#if collection}
    {#each collection as anime}
        <Item class="basis-auto">
            <AnimeCard {anime}/>
        </Item>
    {/each}
{:else}
    {#each Array(20) as i}
        <Item class="basis-auto">
            <Skeleton class="aspect-[420/600] h-full w-48 rounded-t-[0.75rem]"/>
        </Item>
    {/each}
{/if}
