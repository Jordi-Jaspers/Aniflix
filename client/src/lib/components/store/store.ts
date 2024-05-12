import { type Writable, writable } from 'svelte/store';

export const useAuthenticated: Writable<boolean> = writable(false);
export const useHasAuthError: Writable<boolean> = writable(false);
export const useShowInfoModal: Writable<boolean> = writable(false);
export const useModalInfo: Writable<AnimeResponse> = writable({} as AnimeResponse);

export const useShowSearchResults: Writable<boolean> = writable(false);
export const useSearchTerm: Writable<string> = writable('');
