import {type Writable, writable} from 'svelte/store';
import {getContext, setContext} from "svelte";

export async function setAnime(anime: AnimeResponse | RecommendationResponse): Promise<void> {
    setContext('anime', writable(anime));
}

export function getAnime(): Writable<AnimeResponse | RecommendationResponse> {
    return getContext<Writable<AnimeResponse>>('anime');
}

export async function setAnilistId(anilistId: number): Promise<void> {
    setContext('anilistId', writable(anilistId));
}

export function getAnilistId(): Writable<number> {
    return getContext<Writable<number>>('anilistId');
}

