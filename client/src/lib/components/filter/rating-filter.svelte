<script lang="ts">
	import { Button } from '$lib/components/ui/button/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import { Label } from '$lib/components/ui/label/index.js';
	import * as Popover from '$lib/components/ui/popover/index.js';
	import { round } from '$lib/utils';

	export let minRating: number = 0;
	export let maxRating: number = 0;

	let minInputRating: number = 0;
	let maxInputRating: number = 0;

	let isActive = false;
	$: {
		isActive = minRating > 0 || maxRating > 0;
	}

	$: {
		if (minInputRating > 0) {
			minRating = round(minInputRating, 1) * 10;
		}
		if (maxInputRating > 0) {
			maxRating = round(maxInputRating, 1) * 10;
		}
	}
</script>

<Popover.Root portal={null}>
	<Popover.Trigger asChild let:builder>
		<Button builders={[builder]} class={isActive ? 'border-primary' : ''} variant="outline">Rating</Button>
	</Popover.Trigger>
	<Popover.Content class="w-80">
		<div class="grid gap-4">
			<div class="space-y-2">
				<h4 class="font-medium leading-none">Rating</h4>
				<p class="text-sm text-muted-foreground">Set the rating to find anime with a specific rating.</p>
			</div>
			<div class="grid gap-2">
				<div class="grid grid-cols-3 items-center gap-4">
					<Label for="minRating">Min</Label>
					<Input type="number" id="minRating" max="10" placeholder="7.0" step="0.1" bind:value={minInputRating} class="col-span-2 h-8" />
				</div>
				<div class="grid grid-cols-3 items-center gap-4">
					<Label for="maxRating">Max</Label>
					<Input type="number" id="maxRating" max="10" placeholder="10.0" step="0.1" bind:value={maxInputRating} class="col-span-2 h-8" />
				</div>
			</div>
		</div>
	</Popover.Content>
</Popover.Root>
