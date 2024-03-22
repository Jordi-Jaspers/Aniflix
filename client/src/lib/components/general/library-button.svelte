<script lang="ts">
    import {Heart, HeartOff} from 'lucide-svelte';
    import {Button} from "$lib/components/ui/button";
    import {getAnime} from "$lib/components/store/anime-context-store";
    import type {Writable} from "svelte/store";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";

    // https://www.youtube.com/watch?v=V0VfR0eaz98&list=WL&index=84&ab_channel=Joshtriedcoding
    const anime: Writable<AnimeResponse> = getAnime();
    let isRequesting = false;

    async function handleLike() {
        if (isRequesting) return; // Ignore if a request is already in progress
        isRequesting = true;

        try {
            if ($anime.liked) {
                $anime.liked = false;
                const response: Response = await curl(
                    SERVER_URLS.ANI.replace('{id}', $anime.anilistId.toString()),
                    { method: 'POST' }
                );

                if (!response.ok) {
                    $anime.liked = true; // Revert the optimistic update if the request fails
                }
            } else {
                $anime.liked = true;
                const response: Response = await curl(
                    SERVER_URLS.ANIME_LIKE_PATH.replace('{id}', $anime.anilistId.toString()),
                    { method: 'POST' }
                );

                if (!response.ok) {
                    $anime.liked = false; // Revert the optimistic update if the request fails
                }
            }
        } finally {
            isRequesting = false; // Reset the flag regardless of success or failure
        }
    }
</script>

<Button class="h-[40px] w-[40px] flex p-2 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:bg-[#2a2a2a]/60 hover:border-white"
        on:click={handleLike}>
    {#if $anime.inLibrary}
        <BookOpenCheck class="h-[24px] w-[24px]"/>
    {:else}
        <BookOpen class="h-[24px] w-[24px]"/>
    {/if}
</Button>
