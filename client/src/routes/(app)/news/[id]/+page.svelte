<script lang="ts">
	import { onMount } from 'svelte';
	import { curl } from '$lib/api/client';
	import { SERVER_URLS } from '$lib/api/paths';
	import { page } from '$app/stores';
	import { Clock, Link } from 'lucide-svelte';
	import { Badge } from '$lib/components/ui/badge';
	import { getNewsTopicColor } from '$lib/api/constants';
	import { Content, Root, Trigger } from '$lib/components/ui/tooltip/index.js';
	import { toast } from 'svelte-sonner';

	let post: NewsPostResponse;
	onMount(async () => {
		const id: string = $page.params.id.toString();
		const response: Response = await curl(SERVER_URLS.NEWS_DETAILS_PATH.replace('{id}', id), { method: 'GET' });

		if (response.ok) {
			post = await response.json();
		}
	});

	// Function to get time since upload
	function calculateReadingTime(text: string, wordsPerMinute: number = 150): number {
		const words = text.trim().split(/\s+/).length;
		const minutes = words / wordsPerMinute;
		return Math.ceil(minutes);
	}

	// Function to copy link to clipboard
	function copyToClipboard() {
		try {
			navigator.clipboard.writeText(window.location.href);
			toast.success('Successfully copied link to clipboard.');
		} catch (err) {
			toast.error('Failed to copy link to clipboard.');
		}
	}
</script>

{#if post}
	<div class="mx-auto w-full max-w-[1096px] px-[4%]">
		<img src={post.thumbnail} alt={post.title} class="mb-8 h-[20rem] w-full rounded-[0.75rem] object-cover" />

		<div class="space-y-4">
			<h1>{post.title}</h1>
			<div class="flex space-x-2">
				<Badge class="flex w-fit items-center space-x-1" style="background-color: {getNewsTopicColor(post.topic)}">
					<p class="text-sm text-muted dark:text-white">{post.topic}</p>
				</Badge>
				<Badge class="flex w-fit  items-center space-x-1 bg-foreground/60 hover:bg-foreground/60">
					<Clock class="aspect-square h-4 w-auto text-muted" />
					<p class="text-sm text-muted">{calculateReadingTime(post.description + post.intro)} min</p>
				</Badge>
				<Badge class="w-fit items-center  bg-foreground/60 hover:bg-foreground/60">
					<p class="text-sm text-muted">
						{new Date(post.uploadedAt).toLocaleTimeString()}
						, {new Date(post.uploadedAt).toDateString()}
					</p>
				</Badge>

				<Root openDelay="100">
					<Trigger>
						<button class="items-center" on:click={copyToClipboard}>
							<Link class="aspect-square h-4 w-auto" />
						</button>
					</Trigger>
					<Content>
						<p>Copy link to clipboard</p>
					</Content>
				</Root>
			</div>
		</div>

		<div class="[&:not(:first-child)]:mt-6">
			<p class="text-justify leading-7 text-muted-foreground">
				{post.intro}
			</p>
			<p class=" text-justify leading-7 [&:not(:first-child)]:mt-6">{post.description}</p>
			<p class="mt-6 text-justify italic leading-7">source: <a class="hover:underline" href={post.url}>Anime News Network</a></p>
		</div>
	</div>
{/if}
