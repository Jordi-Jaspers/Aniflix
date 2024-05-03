<script lang="ts">
    import {Bookmark, BookmarkCheck} from 'lucide-svelte';
    import {getAnime, setAnime} from "$lib/components/store/anime-context-store";
    import type {Writable} from "svelte/store";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";

    export let value: AnimeResponse | RecommendationResponse;
    setAnime(value);

    const anime: Writable<AnimeResponse | RecommendationResponse> = getAnime();
    let isRequesting = false;

    // https://www.youtube.com/watch?v=V0VfR0eaz98&list=WL&index=84&ab_channel=Joshtriedcoding
    async function handleInLibrary() {
        if (isRequesting) return; // Ignore if a request is already in progress
        isRequesting = true;

        try {
            if ($anime.inLibrary) {
                $anime.inLibrary = false;
                const response: Response = await curl(
                    SERVER_URLS.ANIME_REMOVE_LIBRARY_PATH.replace('{id}', $anime.anilistId.toString()),
                    {method: 'POST'}
                );

                if (!response.ok) {
                    $anime.inLibrary = true; // Revert the optimistic update if the request fails
                }
            } else {
                $anime.inLibrary = true;
                const response: Response = await curl(
                    SERVER_URLS.ANIME_ADD_LIBRARY_PATH.replace('{id}', $anime.anilistId.toString()),
                    {method: 'POST'}
                );

                if (!response.ok) {
                    $anime.inLibrary = false; // Revert the optimistic update if the request fails
                }
            }
        } finally {
            isRequesting = false; // Reset the flag regardless of success or failure
        }
    }
</script>

{#if $anime && $anime.inLibrary}
    <button on:click={handleInLibrary}>
        <BookmarkCheck
                class="w-auto aspect-square h-full p-1.5 rounded-full border-2 text-primary border-primary bg-card/60 transition hover:opacity-75"/>
    </button>
{:else}
    <button on:click={handleInLibrary}>
        <Bookmark
                class="w-auto aspect-square h-full p-1.5 rounded-full border-2 border-card-foreground dark:border-[gray] dark:hover:border-primary/60 bg-card/60 transition hover:text-primary/60 hover:border-primary/60"/>
    </button>
{/if}
