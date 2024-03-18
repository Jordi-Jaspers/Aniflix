<script lang="ts">
	import Logo from '$lib/assets/icons/aniflix-logo-large.png';
	import SearchBar from '$lib/components/app/header/search-bar.svelte';
	import UserButton from '$lib/components/app/header/user-button.svelte';
	import { onMount } from 'svelte';

	let tabs = ["browse", "news", "library"]
	let selected = tabs[0]

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

<header class="!z-50 sticky top-0 px-[4%] lg:px-[60px] h-[5vw] max-h-[75px] min-h-[60px] w-full items-center flex flex-row justify-between overflow-hidden transition-colors duration-300 {isScrolled ? 'bg-background/80' : 'bg-transparent bg-gradient-to-b from-background to-transparent'}">
	<div class="flex flex-row space-x-4 items-center">
		<img alt="AniFlix Logo" class="h-full max-h-[32px]" src={Logo} />
		<div class="font-light text-xs hidden md:flex flex-row space-x-4">
			<a on:click={()=>selected = tabs[0]} class="{selected === 'browse'? 'font-bold' : ''}" href="/browse">Browse</a>
			<a on:click={()=>selected = tabs[1]} class="{selected === 'news'? 'font-bold' : ''}" href="/news">News</a>
			<a on:click={()=>selected = tabs[2]} class="{selected === 'library'? 'font-bold' : ''}" href="/library">Library</a>
		</div>
	</div>

	<div class="flex flex-row items-center space-x-4 h-full">
		<SearchBar />
		<UserButton />
	</div>
</header>

