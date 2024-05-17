<script lang="ts">
	import { Button } from '$lib/components/ui/button/index.js';
	import { Check } from 'lucide-svelte';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu/index.js';

	export let watchStatus: string[] = [];
	export let possibleWatchStatus: string[];
	function handleCheckboxChange(event: Event, status: string) {
		const isChecked = (event.target as HTMLInputElement).checked;
		if (isChecked) {
			watchStatus = [...watchStatus, status];
		} else {
			watchStatus = watchStatus.filter((s) => s !== status);
		}
	}

	let isActive = false;
	$: {
		isActive = watchStatus.length > 0;
	}
</script>

<DropdownMenu.Root>
	<DropdownMenu.Trigger asChild let:builder>
		<Button variant="outline" class={isActive ? 'border-primary' : ''} builders={[builder]}>Watch Status</Button>
	</DropdownMenu.Trigger>
	<DropdownMenu.Content class="max-h-48 w-56 overflow-y-auto scroll-smooth">
		{#each possibleWatchStatus as item}
			<div class="flex cursor-pointer items-center space-x-2 rounded-md p-2 hover:bg-muted">
				<Check class={watchStatus.includes(item) ? 'text-foreground' : 'text-transparent'} />
				<label class="relative flex w-full items-center text-sm">
					<input
						class="hidden"
						checked={watchStatus.includes(item)}
						type="checkbox"
						on:change={(event) => handleCheckboxChange(event, item)}
					/>
					<span>
						{item
							.replace(/_/g, ' ')
							.toLowerCase()
							.replace(/\b\w/g, (l) => l.toUpperCase())}
					</span>
				</label>
			</div>
		{/each}
	</DropdownMenu.Content>
</DropdownMenu.Root>
