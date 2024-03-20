import {type Writable, writable} from 'svelte/store';

export const useAuthenticated:Writable<boolean> = writable(false);
export const useHasAuthError:Writable<boolean> = writable(false);
export const useErrorMessage:Writable<string> = writable('');
export const useHasError:Writable<boolean> = writable(false);
export const useShowInfoModal:Writable<boolean> = writable(false);
export const useAnimeInfo:Writable<AnimeResponse> = writable({} as AnimeResponse);
