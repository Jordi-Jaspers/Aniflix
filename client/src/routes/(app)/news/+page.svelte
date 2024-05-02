<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {NewsFeedCard} from "$lib/components/newsfeed/index.js";

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

    // Function to get time since upload
    function getTimeSinceUpload(date: Date) {
        const now = new Date();
        const diff = now.getTime() - date.getTime();
        const diffInMinutes = Math.floor(diff / 60000);
        if (diffInMinutes < 60) {
            return `${diffInMinutes} minutes ago`;
        }
        const diffInHours = Math.floor(diffInMinutes / 60);
        if (diffInHours < 24) {
            return `${diffInHours} hours ago`;
        }
        const diffInDays = Math.floor(diffInHours / 24);
        return `${diffInDays} days ago`;
    }

    // Function to handle window resize
    function handleResize() {
        setNewsFeed([...bannerCards, ...listCards]);
    }

    // Call setNewsFeed on mount and resize
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.NEWS_PATH, {method: 'GET'});
        if (response.ok) {
            setNewsFeed(await response.json());
        }

        window.addEventListener('resize', handleResize);
    });

    // Remove event listener on component destroy
    onDestroy(() => {
        window.removeEventListener('resize', handleResize);
    });
</script>
<!--<pre>{JSON.stringify(post, null, 4)}</pre>-->

<div class="px-[4%] max-w-[1024px] w-full mx-auto">
    <h1 class="text-3xl font-bold border-l-2 border-primary px-4 my-4">Recent News</h1>

    <!-- Grid view on desktop, single card on mobile, reactive to resizing-->
    {#if !isMobile}
        <div class="h-[32rem]">
            <div class="grid grid-cols-3 grid-rows-4 gap-4 h-full">
                <div class="row-span-4">
                    <NewsFeedCard post={bannerCards[0]}/>
                </div>
                <div class="row-span-2 col-start-2 row-start-3">
                    <NewsFeedCard post={bannerCards[1]}/>
                </div>
                <div class="col-span-2 row-span-2 col-start-2 row-start-1">
                    <NewsFeedCard post={bannerCards[2]}/>
                </div>
                <div class="row-span-2 col-start-3 row-start-3">
                    <NewsFeedCard post={bannerCards[3]}/>
                </div>
            </div>
        </div>

        <!-- Seperator -->
        <div class="border-t-2 border-secondary my-8"/>
    {/if}


    <!-- List view -->
    <div class="grid gap-4" style="grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));">
        {#each listCards as post}
            <a href="{post.url}">
                <div class="w-full rounded-[0.75rem] flex flex-col text-center space-y-4 hover:bg-muted md:h-[36rem] p-4 md:p-2">
                    <img
                            src="{post.thumbnail}"
                            alt="{post.title}"
                            class="aspect-square w-full rounded-[0.75rem] object-cover"
                    />
                    <div class="space-y-2">
                        <div class="flex text-center items-center space-x-2 text-muted-foreground font-extralight text-xs">
                            <span class="w-3 h-3 bg-primary rounded-full"/>
                            <p>{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</p>
                            <span class="w-1 h-1 rounded-full bg-muted-foreground"/>
                            <p>{getTimeSinceUpload(new Date(post.uploadedAt))}</p>
                        </div>
                        <div class="text-left h-full w-full ">
                            <h2 class="text-lg font-bold">{post.title}</h2>
                            <p class="text-sm text-muted-foreground leading-2 mt-2">{post.intro}</p>
                        </div>
                    </div>
                </div>
            </a>
        {/each}
    </div>
</div>

