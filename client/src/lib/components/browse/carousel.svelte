<script lang="ts">
	import { Content, Next, Previous, Root } from '$lib/components/ui/carousel';
	import { ChevronRight } from 'lucide-svelte';

	export let title: string;
	export let genre: string = '';
</script>

<div class="px-[4%] pt-8 lg:px-[60px]">
	<div class="flex justify-between py-2">
		<h1 class="py-1 text-lg font-bold">{genre} {title}</h1>
		<button class="flex">
			See All
			<ChevronRight />
		</button>
	</div>
	<Root class="w-full">
		<Content>
			<slot />
		</Content>
		<Previous class="absolute inset-0 left-[-2%] top-[50%] hidden md:flex " />
		<Next class="absolute right-[-2%] top-[50%] hidden md:flex" />
	</Root>
</div>
