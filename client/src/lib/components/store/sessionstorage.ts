import { type Writable, writable } from 'svelte/store';
import { browser } from '$app/environment';

class SessionStorage<T> {
	key: string;
	value: Writable<T>;

	constructor(key: string, initialValue: any) {
		this.key = key;
		this.value = writable(initialValue);

		if (browser) {
			// Load the initial value from localStorage
			const storedValue = sessionStorage.getItem(this.key);
			if (storedValue) {
				this.value.set(JSON.parse(storedValue));
			}

			// Subscribe to store updates and sync with localStorage
			this.value.subscribe((value) => {
				sessionStorage.setItem(this.key, JSON.stringify(value));
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
		sessionStorage.removeItem(this.key);
	}
}

const useAccessTokenStore: SessionStorage<string> = new SessionStorage('ANIFLIX_ACCESS_TOKEN', '');
export const accessToken: Writable<string> = useAccessTokenStore.get();
