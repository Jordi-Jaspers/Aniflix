<script lang="ts">
    import Logo from '$lib/assets/icons/aniflix-logo-large.png';
    import SearchBar from '$lib/components/app/header/search-bar.svelte';
    import UserButton from '$lib/components/app/header/user-button.svelte';
    import {onMount} from 'svelte';
    import {page} from '$app/stores';
    import {LightSwitch} from "$lib/components/general/index.js";
    import {Badge} from "$lib/components/ui/badge";

    let currentPath = "";
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

<header class="!z-50 sticky top-0 px-[4%] lg:px-[60px] h-[5vw] max-h-[75px] min-h-[60px] w-full items-center flex flex-row justify-between overflow-hidden transition-colors duration-300 {isScrolled ? 'bg-background/80' : 'bg-transparent bg-gradient-to-b from-background to-transparent'}">
    <div class="flex flex-row space-x-4 items-center">
        <div class="flex items-center space-x-1">
            <img alt="AniFlix Logo" class="h-full max-h-[32px]" src={Logo}/>
            {#if import.meta.env.VITE_ENV !== 'production'}
                <Badge variant="outline" class="bg-green-600 my-3">
                    TST
                </Badge>
            {/if}
        </div>

        <div class="font-light text-xs hidden md:flex flex-row space-x-4">
            <a class="{currentPath.includes('/browse') ? 'font-bold' : ''}" href="/browse">Browse</a>
            <a class="{currentPath.includes('/news') ? 'font-bold' : ''}" href="/news">News</a>
            <a class="{currentPath.includes('/library') ? 'font-bold' : ''}" href="/library">Library</a>
        </div>
    </div>

    <div class="flex flex-row items-center md:space-x-2 h-full">
        <SearchBar/>
        <LightSwitch/>
        <UserButton/>
    </div>
</header>

