<script lang="ts">
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js';
	import { Button } from '$lib/components/ui/button/index.js';
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { Check } from 'lucide-svelte';

	export let status: string[] = [];
	export let possibleStatus: string[] = [];
	function handleCheckboxChange(event: Event, selected: string) {
		const isChecked = (event.target as HTMLInputElement).checked;
		if (isChecked) {
			status = [...status, selected];
		} else {
			status = status.filter((s) => s !== selected);
		}
	}

	let isActive = false;
	$: {
		isActive = status.length > 0;
	}
</script>

<DropdownMenu.Root>
	<DropdownMenu.Trigger asChild let:builder>
		<Button variant="outline" class={isActive ? 'border-primary' : ''} builders={[builder]}>Status</Button>
	</DropdownMenu.Trigger>
	<DropdownMenu.Content class="max-h-48 w-56 overflow-y-auto scroll-smooth">
		{#each possibleStatus as item}
			<div class="flex cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-muted">
				<Check class={status.includes(item) ? 'text-foreground' : 'text-transparent'} />
				<label class="relative flex w-full items-center text-sm">
					<input class="hidden" checked={status.includes(item)} type="checkbox" on:change={(event) => handleCheckboxChange(event, item)} />
					<span
						>{item
							.replace(/_/g, ' ')
							.toLowerCase()
							.replace(/\b\w/g, (l) => l.toUpperCase())}</span
					>
				</label>
			</div>
		{/each}
	</DropdownMenu.Content>
</DropdownMenu.Root>
