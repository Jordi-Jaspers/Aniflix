<script lang="ts">
import { getNewsTopicColor } from '$lib/api/constants';
import { goto } from '$app/navigation';
import { CLIENT_URLS } from '$lib/api/paths';

export let post: NewsPostResponse;
</script>

{#if post}
	<div class="relative h-full overflow-hidden rounded-[0.75rem] border-2 hover:border-primary">
		<button class="absolute h-full w-full" on:click={() => goto(CLIENT_URLS.NEWS_URL + '/' + post.id.toString())} />
		<div class="h-full w-full bg-cover bg-center bg-no-repeat" style="background-image: url({post.thumbnail})">
			<div class="h-full w-full bg-gradient-to-b from-transparent to-black/40 lg:to-[99%]">
				<div class="h-full w-full bg-gradient-to-t from-transparent to-black/20 lg:to-[99%]" />
			</div>
		</div>
		<p class="absolute left-2 top-2 flex items-center space-x-2 text-white shadow-2xl">
			<span class="h-3 w-3 rounded-full" style="background-color: {getNewsTopicColor(post.topic)}" />
			<span class="text-sm font-semibold shadow-2xl">{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</span>
		</p>

		<h4 class="text-md absolute bottom-4 px-4 text-white">
			{post.title}
		</h4>
	</div>
{/if}
