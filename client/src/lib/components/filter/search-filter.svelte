<script lang="ts">
import { CircleX, ScanSearch } from 'lucide-svelte';

export let query: string = '';
</script>

<div id="search" class="flex h-10 items-center justify-between rounded-md border border-input bg-transparent px-2 text-sm md:w-1/2">
	<input type="text" bind:value={query} placeholder="Search for anime..." class="h-full w-full bg-transparent text-sm focus:outline-none" />
	<div class="flex space-x-2">
		<ScanSearch />
		<button class="hover:text-primary" on:click={() => (query = '')}>
			<CircleX />
		</button>
	</div>
</div>
