import {type Writable, writable} from 'svelte/store';
import {getContext, setContext} from "svelte";
import {curl} from "$lib/api/client";
import {SERVER_URLS} from "$lib/api/paths";


export async function setAnime(anime: AnimeResponse): Promise<void> {
    setContext('anime', writable(anime));
}

export function getAnime(): Writable<AnimeResponse> {
    return getContext<Writable<AnimeResponse>>('anime');
}
