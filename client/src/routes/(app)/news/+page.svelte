<script lang="ts">
	import { onDestroy, onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { NewsFeedCard, NewsFeedItem } from '$lib/components/newsfeed/index';
	import {LoadingScreen} from "$lib/components/general";

	let bannerCards: NewsPostResponse[] = [];
	let listCards: NewsPostResponse[] = [];
	let isMobile: boolean = false;


	// Function to set news feed based on window width
	function setNewsFeed(newsFeed: NewsPostResponse[]) {
		const mobile_threshold = 768;
		if (window.innerWidth < mobile_threshold) {
			bannerCards = [];
			listCards = newsFeed;
			isMobile = true;
		} else {
			bannerCards = newsFeed.slice(0, 4);
			listCards = newsFeed.slice(4);
			isMobile = false;
		}
	}

	// Function to handle window resize
	function handleResize() {
		setNewsFeed([...bannerCards, ...listCards]);
	}

	// Call setNewsFeed on mount and resize
	let isLoading: boolean = true;
	onMount(async () => {
		const response: Response = await curl(SERVER_URLS.NEWS_PATH, { method: 'GET' });
		if (response.ok) {
			setNewsFeed(await response.json());
		}

		window.addEventListener('resize', handleResize);
		isLoading = false;
	});

	// Remove event listener on component destroy
	onDestroy(() => {
		window.removeEventListener('resize', handleResize);
	});
</script>

<svelte:head>
	<title>News - Aniflix</title>
	<meta name="description" content="The latest news and updates from the Aniflix team." />
	<meta name="keywords" content="Aniflix, news, updates, anime, manga, community" />
</svelte:head>

{#if isLoading}
	<LoadingScreen />
{/if}

<div class="mx-auto w-full max-w-[1096px] px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">Recent News</h1>

	<!-- Grid view on desktop, single card on mobile, reactive to resizing-->
	{#if !isMobile}
		<div class="h-[32rem]">
			<div class="grid h-full grid-cols-3 grid-rows-4 gap-4">
				<div class="row-span-4">
					<NewsFeedCard post={bannerCards[0]} />
				</div>
				<div class="col-start-2 row-span-2 row-start-3">
					<NewsFeedCard post={bannerCards[1]} />
				</div>
				<div class="col-span-2 col-start-2 row-span-2 row-start-1">
					<NewsFeedCard post={bannerCards[2]} />
				</div>
				<div class="col-start-3 row-span-2 row-start-3">
					<NewsFeedCard post={bannerCards[3]} />
				</div>
			</div>
		</div>
		<div class="my-8 border-t-2 border-secondary" />
	{/if}

	<div class="grid gap-4" style="grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));">
		{#each listCards as post}
			<NewsFeedItem {post} />
		{/each}
	</div>
</div>
