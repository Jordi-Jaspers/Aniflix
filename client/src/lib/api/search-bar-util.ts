import { useOpenSearchBar, useSearchTerm } from '$lib/components/store/store';

export function closeSearchBar() {
	const input = document.getElementById('search-bar') as HTMLInputElement;
	if (input) {
		input.blur();
		useOpenSearchBar.set(false);
		useSearchTerm.set('');
	}
}

export function openSearchBar() {
	const input = document.getElementById('search-bar') as HTMLInputElement;
	if (input) {
		input.focus();
		useOpenSearchBar.set(true);
	}
}
