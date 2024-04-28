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
                    SERVER_URLS.ANIME_DISLIKE_PATH.replace('{id}', $anime.anilistId.toString()),
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

{#if $anime.liked}
    <button on:click={handleLike}>
        <Heart class="w-auto aspect-square h-full p-1 rounded-full border-2 text-primary border-primary bg-[#2a2a2a]/60 transition hover:opacity-75"/>
    </button>
{:else}
    <button on:click={handleLike}>
        <HeartOff class="w-auto aspect-square h-full p-1 rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:text-primary/60 hover:border-primary/60"/>
    </button>
{/if}
