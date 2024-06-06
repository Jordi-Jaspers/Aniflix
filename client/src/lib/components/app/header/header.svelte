<script lang="ts">
import FullLogo from '$lib/assets/icons/aniflix-logo-small.webp?enhanced';
import SmallLogo from '$lib/assets/icons/aniflix-icon-small.webp?enhanced';
import SearchBar from '$lib/components/app/header/search-bar.svelte';
import UserButton from '$lib/components/app/header/user-button.svelte';
import { onMount } from 'svelte';
import { page } from '$app/stores';
import { ProviderStatusButton } from '$lib/components/general/index.js';
import { Badge } from '$lib/components/ui/badge';

let currentPath = '';
$: currentPath = $page.url.pathname;

let isScrolled: boolean = false;
onMount(() => {
	const handleScroll = () => {
		isScrolled = window.scrollY > 0;
	};
	window.addEventListener('scroll', handleScroll);
	return () => {
		window.removeEventListener('scroll', handleScroll);
	};
});
</script>

<header
	class="sticky top-0 !z-50 flex h-[5vw] max-h-[75px] min-h-[60px] w-full flex-row items-center justify-between overflow-hidden px-[4%] transition-colors duration-300 lg:px-[60px] {isScrolled
		? 'bg-background/80'
		: 'bg-transparent bg-gradient-to-b from-background to-transparent'}"
>
	<div class="flex flex-row items-center space-x-4">
		<div class="flex items-center space-x-1">
			<enhanced:img alt="AniFlix Full Logo" class="hidden aspect-[220/85] h-full max-h-[32px] w-auto md:block" src={FullLogo} />
			<enhanced:img alt="AniFlix Small Logo" class="block h-full max-h-[32px] md:hidden" src={SmallLogo} />
			{#if import.meta.env.VITE_ENV === 'test'}
				<Badge variant="outline" class="my-3 bg-blue-600">TST</Badge>
			{/if}

			{#if import.meta.env.VITE_ENV === 'development'}
				<Badge variant="outline" class="my-3 bg-green-600">DEV</Badge>
			{/if}
		</div>

		<div class="hidden flex-row space-x-4 text-xs font-light md:flex">
			<a data-sveltekit-preload-data="hover" class={currentPath.includes('/browse') ? 'font-bold' : ''} href="/browse">Browse</a>
			<a data-sveltekit-preload-data="hover" class={currentPath.includes('/news') ? 'font-bold' : ''} href="/news">News</a>
			<a data-sveltekit-preload-data="hover" class={currentPath.includes('/library') ? 'font-bold' : ''} href="/library">Library</a>
		</div>
	</div>

	<div class="flex h-full flex-row items-center md:space-x-2">
		<SearchBar />
		<ProviderStatusButton />
		<UserButton />
	</div>
</header>
