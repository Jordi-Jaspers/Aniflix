<script lang="ts">
    import {useSearchTerm, useShowSearchResults} from "$lib/components/store/store";
    import {curl} from "$lib/api/client";
    import {CLIENT_URLS, SERVER_URLS} from "$lib/api/paths";
    import {SearchResult} from "$lib/components/search/index";
    import {ChevronRight} from "lucide-svelte";
    import {debounce} from "$lib/utils";
    import {goto} from "$app/navigation";

    let page = 1;
    let searchResults: AnimeResponse[] = [];

    // Subscribe to the search term with debounce
    const debouncedSearch = debounce(async (value: string) => {
        if (!value) {
            return;
        }

        const body: AnimeRequest = {
            page: page,
            perPage: 8,
            title: value,
            genre: null,
            season: null
        };

        const response: Response = await curl(SERVER_URLS.ANIME_SEARCH_PATH, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            searchResults = await response.json();
        }
    }, 500); // Adjust delay as needed (e.g., 500 milliseconds)

    // Subscribe to debounced search function
    useSearchTerm.subscribe(debouncedSearch);
</script>

{#if $useShowSearchResults}
    <div class="flex flex-col px-[4%] max-w-[1096px] w-full mx-auto py-4 space-y-4" >
        <div class="flex justify-between">
            <h1 class="text-3xl font-bold border-l-2 border-primary px-4">Search Results</h1>
            <button class="flex items-center hover:underline hover:opacity-80" on:click={() => goto(CLIENT_URLS.SEARCH_URL)}>
                <span>Advanced Search</span>
                <ChevronRight class="h-4 w-4"/>
            </button>
        </div>
        <div class="grid gap-4" style="grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));">
            {#each searchResults as result}
                <SearchResult {result}/>
            {/each}
        </div>
    </div>
{/if}
