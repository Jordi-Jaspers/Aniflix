<script lang="ts">
	import { Search } from 'lucide-svelte';
	import Input from '$lib/components/ui/input/input.svelte';
	import {Button} from "$lib/components/ui/button";
	import {useSearchTerm, useShowSearchResults} from "$lib/components/store/store";

	let isOpen: boolean = false;
	function closeSearchBar() {
		const input = document.getElementById('search-bar') as HTMLInputElement;
		if (input) {
			input.blur();
			isOpen = false;
			$useSearchTerm = '';
		}
	}

	function openSearchBar() {
		const input = document.getElementById('search-bar') as HTMLInputElement;
		if (input) {
			input.focus();
			isOpen = true;
		}
	}

	function handleEscape(e: KeyboardEvent) {
		if (e.key === 'Escape') {
			closeSearchBar();
		}
	}

	function toggleSearchBar() {
		if (isOpen) {
			closeSearchBar();
		} else {
			openSearchBar();
		}
	}

	$: $useShowSearchResults = $useSearchTerm.length > 0;
</script>

<div class="flex flex-row items-center">
	<Input
		id="search-bar"
		class="h-6 mr-1 rounded-[0.75rem] {isOpen ? 'w-36 transition-all duration-500 ease-out sm:w-48 bg-background' : 'w-0 border-0 duration-500 ease-in'}"
		placeholder="Search"
		bind:value={$useSearchTerm}
		on:keydown={handleEscape}
	/>
	<Button on:click={toggleSearchBar} variant="ghost" size="icon" class="rounded-[0.75rem]">
		<Search />
	</Button>
</div>
