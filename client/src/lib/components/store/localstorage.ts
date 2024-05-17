import { type Writable, writable } from 'svelte/store';
import { browser } from '$app/environment';

class LocalStorage<T> {
	key: string;
	value: Writable<T>;

	constructor(key: string, initialValue: any) {
		this.key = key;
		this.value = writable(initialValue);

		if (browser) {
			// Load the initial value from localStorage
			const storedValue = localStorage.getItem(this.key);
			if (storedValue) {
				this.value.set(JSON.parse(storedValue));
			}

			// Subscribe to store updates and sync with localStorage
			this.value.subscribe((value) => {
				localStorage.setItem(this.key, JSON.stringify(value));
			});
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

const useModalInfoStore: LocalStorage<AnimeResponse> = new LocalStorage('modalInfo', {});
export const useModalInfo: Writable<AnimeResponse> = useModalInfoStore.get();
