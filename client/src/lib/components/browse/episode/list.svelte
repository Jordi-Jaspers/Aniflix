<script lang="ts">
    import {Content, Item, Root, Trigger, Value} from "$lib/components/ui/select";
    import {EpisodeListCard} from "$lib/components/browse";
    import type {Selected} from "bits-ui";

    export let episodes: EpisodeResponse[];

    const pageSize = 12;
    const totalPages = Math.ceil(episodes.length / pageSize);
    const lowerBound = (page: number) => {
        return page === 1 ? 1 : ((page - 1) * pageSize) + 1;
    }
    const upperBound = (page: number) => {
        if (episodes.length < page * pageSize) {
            return episodes.length;
        } else {
            return page * pageSize;
        }
    }

    let currentPage: Selected<number> = {value: 1, label: "Episodes " + lowerBound(1) + " - " + upperBound(1)};
    function setCurrentPage(selectedPage: Selected<number> | undefined) {
        if (selectedPage) {
            currentPage = selectedPage;
        }
    }
</script>

{ #if episodes && episodes.length > 0}
    <div class="items-center rounded-b-md py-4">
        <Root onSelectedChange={(selectedPage) => setCurrentPage(selectedPage)} selected={currentPage}>
            <Trigger class="min-w-[25%] w-fit space-x-2">
                <Value placeholder="{episodes.length} Episodes"/>
            </Trigger>
            <Content class="z-[1000] w-full max-h-[25%] overflow-y-auto scroll-smooth">
                {#each Array.from({length: totalPages}, (_, i) => i + 1) as i}
                    <Item value={i} label="Episodes {lowerBound(i)} - {upperBound(i)}"/>
                {/each}
            </Content>
        </Root>
    </div>

    {#each episodes as episode}
        {#if episode.episodeNumber >= lowerBound(currentPage.value) && episode.episodeNumber <= upperBound(currentPage.value)}
            <EpisodeListCard episode={episode}/>
        {/if}
    {/each}
{/if}
