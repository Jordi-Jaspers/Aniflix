import { type Writable, writable } from 'svelte/store';
import { getContext, setContext } from 'svelte';

export async function setAnime(anime: AnimeResponse | RecommendationResponse): Promise<void> {
	const animeInfo: AnimeInfo = {
		anilistId: anime.anilistId,
		inLibrary: anime.inLibrary,
		liked: anime.liked
	};

	setContext('anime', writable(animeInfo));
}

export function getAnimeInfo(): Writable<AnimeInfo> {
	return getContext<Writable<AnimeResponse>>('anime');
}

export async function setAnilistId(anilistId: number): Promise<void> {
	setContext('anilistId', writable(anilistId));
}

export function getAnilistId(): Writable<number> {
	return getContext<Writable<number>>('anilistId');
}
