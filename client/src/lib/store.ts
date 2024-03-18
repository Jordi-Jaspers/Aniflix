import {writable} from 'svelte/store';

export const useAuthenticated = writable(false);
export const useErrorMessage = writable('');
export const useHasError = writable(false);

export const useIsLoading = writable(false);
