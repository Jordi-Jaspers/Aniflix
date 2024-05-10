<script lang="ts">
	import { Skeleton } from '$lib/components/ui/skeleton/index.js';
	import { getNewsTopicColor } from '$lib/api/constants';
	import { goto } from '$app/navigation';
	import { CLIENT_URLS } from '$lib/api/paths';

	export let post: NewsPostResponse;

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
</script>

{#if post}
	<div class="relative flex w-full flex-col space-y-4 rounded-[0.75rem] p-4 text-center hover:bg-muted md:h-[36rem] md:p-2">
		<button class="absolute h-full w-full" on:click={() => goto(CLIENT_URLS.NEWS_URL + '/' + post.id.toString())} />
		<img class="aspect-square h-auto w-full rounded-[0.75rem] object-cover" src={post.thumbnail} alt={post.title} />
		<div class="space-y-2 overflow-hidden">
			<div class="flex items-center space-x-2 text-center text-xs font-extralight text-muted-foreground">
				<span class="h-3 w-3 rounded-full" style="background-color: {getNewsTopicColor(post.topic)}" />
				<p>{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</p>
				<span class="h-1 w-1 rounded-full bg-muted-foreground" />
				<p>{getTimeSinceUpload(new Date(post.uploadedAt))}</p>
			</div>
			<div class="h-full w-full text-left">
				<h4>{post.title}</h4>
				<p class="leading-2 mt-2 text-sm text-muted-foreground">{post.intro}</p>
			</div>
		</div>
	</div>
{:else}
	<Skeleton />
{/if}
