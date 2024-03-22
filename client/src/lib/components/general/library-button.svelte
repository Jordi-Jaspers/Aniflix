<script lang="ts">
    import {BookOpen, BookOpenCheck} from 'lucide-svelte';
    import {Button} from "$lib/components/ui/button";
    import {getAnime} from "$lib/components/store/anime-context-store";
    import type {Writable} from "svelte/store";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";

    // https://www.youtube.com/watch?v=V0VfR0eaz98&list=WL&index=84&ab_channel=Joshtriedcoding
    const anime: Writable<AnimeResponse> = getAnime();
    let isRequesting = false;

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

<Button class="h-[40px] w-[40px] flex p-2 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:bg-[#2a2a2a]/60 hover:border-white"
        on:click={handleInLibrary}>
    {#if $anime.inLibrary}
        <BookOpenCheck class="h-[24px] w-[24px] text-green-600"/>
    {:else}
        <BookOpen class="h-[24px] w-[24px]"/>
    {/if}
</Button>
