<script lang="ts">
	import Logo from '$lib/assets/icons/aniflix-logo-large.png';
	import SearchBar from '$lib/components/app/header/search-bar.svelte';
	import UserButton from '$lib/components/app/header/user-button.svelte';
	import { onMount } from 'svelte';
	import { page } from '$app/stores';
	import { LightSwitch } from '$lib/components/general/index.js';
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
			<img alt="AniFlix Logo" class="h-full max-h-[32px]" src={Logo} />
			{#if import.meta.env.VITE_ENV !== 'production'}
				<Badge variant="outline" class="my-3 bg-green-600">TST</Badge>
			{/if}
		</div>

		<div class="hidden flex-row space-x-4 text-xs font-light md:flex">
			<a class={currentPath.includes('/browse') ? 'font-bold' : ''} href="/browse">Browse</a>
			<a class={currentPath.includes('/news') ? 'font-bold' : ''} href="/news">News</a>
			<a class={currentPath.includes('/library') ? 'font-bold' : ''} href="/library">Library</a>
		</div>
	</div>

	<div class="flex h-full flex-row items-center md:space-x-2">
		<SearchBar />
		<LightSwitch />
		<UserButton />
	</div>
</header>
