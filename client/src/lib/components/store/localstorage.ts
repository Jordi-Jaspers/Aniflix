import { type Writable, writable } from 'svelte/store';
import { browser } from '$app/environment';
import toast from 'svelte-french-toast';
import type { Selected } from 'bits-ui';

class LocalStorage<T> {
	key: string;
	value: Writable<T>;

	constructor(key: string, initialValue: T) {
		this.key = key;
		this.value = writable(initialValue);

		if (browser) {
			// Load the initial value from localStorage
			const storedValue: string | null = localStorage.getItem(this.key);
			if (storedValue) {
				try {
					const parsedValue = this.parseValue(storedValue);
					this.value.set(parsedValue);
				} catch (e) {
					toast.error('Failed to parse stored value');
				}
			}

			// Subscribe to store updates and sync with localStorage
			this.value.subscribe((value) => {
				localStorage.setItem(this.key, this.stringifyValue(value));
			});
		}
	}

	// Determine if value needs to be parsed as JSON or used as-is
	private parseValue(value: string): T {
		// Check if the initialValue is a string type
		if (typeof this.value === 'string' || this.isJSONString(value)) {
			return JSON.parse(value);
		}
		return value as unknown as T;
	}

	// Convert the value to a string for storage in localStorage
	private stringifyValue(value: T): string {
		// Check if the value is a string type
		if (typeof value === 'string') {
			return value;
		}
		return JSON.stringify(value);
	}

	// Check if a string is a valid JSON string
	private isJSONString(value: string): boolean {
		try {
			JSON.parse(value);
			return true;
		} catch {
			return false;
		}
	}

	// Get the underlying store to be used in Svelte components
	get(): Writable<T> {
		return this.value;
	}

	// Update the store value
	set(value: T) {
		this.value.set(value);
	}

	// Update the store value with a callback function
	update(updater: (value: T) => T) {
		this.value.update(updater);
	}

	// Reset the store value to the initial value and clear localStorage
	reset(initialValue: T) {
		this.value.set(initialValue);
		localStorage.removeItem(this.key);
	}
}

const useShowInfoModalStore: LocalStorage<boolean> = new LocalStorage('showInfoModal', false);
export const useShowInfoModal: Writable<boolean> = useShowInfoModalStore.get();

const useModalInfoStore: LocalStorage<AnimeResponse> = new LocalStorage('modalInfo', {} as AnimeResponse);
export const useModalInfo: Writable<AnimeResponse> = useModalInfoStore.get();

const useModalEpisodeSelectorStore: LocalStorage<Selected<number>> = new LocalStorage('modalEpisodeSelector', { value: 0, label: '0' });
export const useModalEpisodeSelector: Writable<Selected<number>> = useModalEpisodeSelectorStore.get();

const useRefreshTokenStore: LocalStorage<string> = new LocalStorage('ANIFLIX_REFRESH_TOKEN', '');
export const refreshToken: Writable<string> = useRefreshTokenStore.get();

const useShowChangelogStore: LocalStorage<boolean> = new LocalStorage('showChangelog', true);
export const useShowChangelog: Writable<boolean> = useShowChangelogStore.get();
