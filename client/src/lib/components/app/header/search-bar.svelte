<script lang="ts">
import { Search } from 'lucide-svelte';
import Input from '$lib/components/ui/input/input.svelte';
import { Button } from '$lib/components/ui/button';
import { useOpenSearchBar, useSearchTerm, useShowSearchResults } from '$lib/components/store/store';
import { closeSearchBar, openSearchBar } from '$lib/api/search-bar-util';

function handleEscape(e: KeyboardEvent) {
	if (e.key === 'Escape') {
		closeSearchBar();
	}
}

export function toggleSearchBar() {
	if ($useOpenSearchBar) {
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
		class="mr-1 h-6 rounded-[0.75rem] {$useOpenSearchBar
			? 'w-36 bg-background transition-all duration-500 ease-out sm:w-48'
			: 'w-0 border-0 duration-500 ease-in'}"
		placeholder="Search"
		bind:value={$useSearchTerm}
		on:keydown={handleEscape}
	/>
	<Button on:click={toggleSearchBar} variant="ghost" size="icon" class="rounded-[0.75rem]">
		<Search />
	</Button>
</div>
