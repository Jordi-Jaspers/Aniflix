<script lang="ts">
	import { Bookmark, BookmarkCheck } from 'lucide-svelte';
	import { getAnimeInfo } from '$lib/components/store/anime-context-store';
	import type { Writable } from 'svelte/store';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';

	const anime: Writable<AnimeInfo> = getAnimeInfo();
	let isRequesting = false;

	// https://www.youtube.com/watch?v=V0VfR0eaz98&list=WL&index=84&ab_channel=Joshtriedcoding
	async function handleInLibrary() {
		if (isRequesting) return; // Ignore if a request is already in progress
		isRequesting = true;

		try {
			if ($anime.inLibrary) {
				$anime.inLibrary = false;
				const response: Response = await curl(SERVER_URLS.ANIME_REMOVE_LIBRARY_PATH.replace('{id}', $anime.anilistId.toString()), {
					method: 'POST'
				});

				if (!response.ok) {
					$anime.inLibrary = true; // Revert the optimistic update if the request fails
				}
			} else {
				$anime.inLibrary = true;
				const response: Response = await curl(SERVER_URLS.ANIME_ADD_LIBRARY_PATH.replace('{id}', $anime.anilistId.toString()), {
					method: 'POST'
				});

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
			class="aspect-square h-full w-auto rounded-full border-2 border-primary bg-card/60 p-1.5 text-primary transition hover:opacity-75"
		/>
	</button>
{:else}
	<button on:click={handleInLibrary}>
		<Bookmark
			class="aspect-square h-full w-auto rounded-full border-2 border-card-foreground bg-card/60 p-1.5 transition hover:border-primary/60 hover:text-primary/60 dark:border-[gray] dark:hover:border-primary/60"
		/>
	</button>
{/if}
