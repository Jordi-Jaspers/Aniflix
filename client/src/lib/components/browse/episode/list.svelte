<script lang="ts">
    import {Content, Item, Root, Trigger, Value} from "$lib/components/ui/select";
    import {EpisodeListCard} from "$lib/components/browse";
    import {useModalInfo} from "$lib/components/store/store";
    import type {Selected} from "bits-ui";

    export let episodes: EpisodeResponse[];

    const totalPages = Math.ceil(episodes.length / 25);
    const lowerBound = (page: number) => {
        return page === 1 ? 1 : ((page - 1) * 25) + 1;
    }
    const upperBound = (page: number) => {
        if (episodes.length < page * 25) {
            return episodes.length;
        } else {
            return page * 25;
        }
    }

    let currentPage: Selected<number> = {value: 1, label: "Page 1 (Episodes " + lowerBound(1) + " - " + upperBound(1) + ")"};
    function setCurrentPage(selectedPage: Selected<number> | undefined) {
        if (selectedPage) {
            currentPage = selectedPage;
        }
    }
</script>

{ #if episodes && episodes.length > 0}
    <div class="relative flex flex-row justify-between items-center rounded-b-md py-4">
        <h4 class="text-2xl font-semibold text-white py-4">Episodes</h4>
        <Root onSelectedChange={(selectedPage) => setCurrentPage(selectedPage)} selected={currentPage}>
            <Trigger class="min-w-[30%] w-fit space-x-2">
                <Value placeholder="{episodes.length} Episodes"/>
            </Trigger>
            <Content class="z-[1000] w-full overflow-hidden">
                {#each Array.from({length: totalPages}, (_, i) => i + 1) as i}
                    <Item value={i} label="Page {i} (Episodes {lowerBound(i)} - {upperBound(i)})"/>
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
