<script lang="ts">
import { onDestroy, onMount } from 'svelte';
import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { NewsFeedCard, NewsFeedItem } from '$lib/components/newsfeed/index';
import { LoadingScreen } from '$lib/components/general';
import { PaginationBar } from '$lib/components/search';

let bannerCards: NewsPostResponse[] = [];
let listCards: NewsPostResponse[] = [];
let isMobile: boolean = false;

// Add event listener on component mount
onMount(async () => {
	await getNewsFeed();
	window.addEventListener('resize', handleResize);
});

// Remove event listener on component destroy
onDestroy(() => {
	window.removeEventListener('resize', handleResize);
});

// Call setNewsFeed on mount and resize
let isLoading: boolean = true;
let pageNumber: number = 1;
let request: PageRequest = { page: 0, perPage: 25 };
let page: PageResponse<NewsPostResponse>;

async function getNewsFeed() {
	const response: Response = await curl(SERVER_URLS.NEWS_PATH, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(request)
	});

	if (response.ok) {
		page = await response.json();
		setNewsFeed(page.content);
		isLoading = false;

		window.scrollTo({ top: 0, behavior: 'smooth' });
	}
}

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

$: if (request) getNewsFeed();
$: request.page = pageNumber - 1;
</script>

<svelte:head>
	<title>News - Aniflix</title>
	<meta name="description" content="The latest news and updates from the Aniflix team." />
	<meta name="keywords" content="Aniflix, news, updates, anime, manga, community" />
</svelte:head>

{#if isLoading}
	<LoadingScreen />
{:else}
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
				<NewsFeedItem post={post} />
			{/each}
		</div>

		<PaginationBar bind:totalElements={page.totalElements} bind:pageNumber={pageNumber} bind:pageSize={request.perPage} />
	</div>
{/if}
