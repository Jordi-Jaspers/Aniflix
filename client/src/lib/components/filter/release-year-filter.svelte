<script lang="ts">
	import { Button } from "$lib/components/ui/button/index.js";
	import { Input } from "$lib/components/ui/input/index.js";
	import { Label } from "$lib/components/ui/label/index.js";
	import * as Popover from "$lib/components/ui/popover/index.js";

	export let beforeYear: number = 0;
	export let afterYear: number = 0;

	let isActive = false;
	$: {
		isActive = beforeYear > 0 || afterYear > 0;
	}
</script>

<Popover.Root portal={null}>
	<Popover.Trigger asChild let:builder>
		<Button builders={[builder]} class="{isActive ? 'border-primary' : ''}" variant="outline">Release Year</Button>
	</Popover.Trigger>
	<Popover.Content class="w-80">
		<div class="grid gap-4">
			<div class="space-y-2">
				<h4 class="font-medium leading-none">Release Year</h4>
				<p class="text-sm text-muted-foreground">
					Set the release year to find anime released in a specific year.
				</p>
			</div>
			<div class="grid gap-2">
				<div class="grid grid-cols-3 items-center gap-4">
					<Label for="afterYear">After</Label>
					<Input type="number" id="afterYear" placeholder="1999" bind:value={afterYear} class="col-span-2 h-8" />
				</div>
				<div class="grid grid-cols-3 items-center gap-4">
					<Label for="beforeYear">Before</Label>
					<Input type="number" id="beforeYear" placeholder="2024" bind:value={beforeYear} class="col-span-2 h-8" />
				</div>
			</div>
		</div>
	</Popover.Content>
</Popover.Root>
