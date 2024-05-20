<script lang="ts">
	import { Content, Next, Previous, Root } from '$lib/components/ui/carousel';
	import { ChevronRight } from 'lucide-svelte';
	import { goto } from '$app/navigation';

	export let type: string = '';
	export let showAll: boolean = true;
</script>

<div class="px-[4%] pt-8 lg:px-[60px]">
	<div class="flex justify-between py-2">
		<h1 class="py-1 text-lg font-bold">{type} Anime</h1>
		<div>
			{#if showAll}
				<button class="flex" on:click={() => goto('/browse/' + type.toLowerCase())}>
					See All
					<ChevronRight />
				</button>
			{/if}
		</div>
	</div>
	<Root class="w-full">
		<Content>
			<slot />
		</Content>
		<Previous class="absolute inset-0 left-[-2%] top-[50%] hidden md:flex " />
		<Next class="absolute right-[-2%] top-[50%] hidden md:flex" />
	</Root>
</div>
