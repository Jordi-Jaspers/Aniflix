import { type Writable, writable } from 'svelte/store';

export const useAuthenticated: Writable<boolean> = writable(false);
export const useHasAuthError: Writable<boolean> = writable(false);

export const useShowSearchResults: Writable<boolean> = writable(false);
export const useSearchTerm: Writable<string> = writable('');
export const useOpenSearchBar: Writable<boolean> = writable(false);

export const useUserDetails: Writable<UserDetailsResponse> = writable({} as UserDetailsResponse);
