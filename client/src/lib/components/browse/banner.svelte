<script lang="ts">
	import { Button } from '$lib/components/ui/button';
	import { InfoIcon, PlayIcon, StarIcon } from 'lucide-svelte';
	import { openModal } from '$lib/api/util';
	import { goto } from '$app/navigation';

	export let anime: AnimeResponse;
	let genres: string[] = anime.genres
		.filter((genre) => genre !== 'UNKNOWN')
		.slice(0, 3)
		.map((genre) => genre.charAt(0) + genre.slice(1).toLowerCase());

	let max_characters: number = 575;
	let description: string = anime.description.replace(/\(Source:.*\)/, '');
	description = description.length > max_characters ? description.substring(0, max_characters) : description;

	let lastOpenTagIndex = description.lastIndexOf('<');
	if (lastOpenTagIndex > 0) {
		description = description.substring(0, lastOpenTagIndex);
	}
</script>

{#if anime}
	<div class="w-80% mx-[4%] my-4 hidden h-[48vw] max-h-[650px] items-center px-[4%] md:flex">
		<img
			class="aspect-[460/650] h-full max-h-[550px] w-auto max-w-[390px] rounded-[0.75rem] shadow-2xl"
			height="650"
			width="460"
			src={anime.image}
			alt="thumbnail"
		/>
		<div class="flex h-fit flex-col p-2 pl-8">
			<div class="space-y-4">
				<h1 class="flex items-center text-2xl font-bold uppercase leading-none xl:text-3xl">{anime.title}</h1>
				<div class="flex items-center text-xs text-muted-foreground xl:text-sm">
					{anime.rating / 10}
					<StarIcon class="ml-[0.5rem] h-4 w-fit fill-amber-300 text-amber-300" />
					<span class="mx-2"> • </span>
					{anime.totalEpisodes} Episodes
					<span class="mx-2"> • </span>
					{genres.join(', ')}
				</div>
				<article class="prose w-[80%] py-2 text-justify text-[1.4vw] font-extralight opacity-75 xl:text-[1.2rem]">
					{@html description}
				</article>
			</div>

			<div class="flex space-x-4 py-8">
				<Button
					class="space-x-4 rounded-full pl-4 pr-1 opacity-80 transition hover:opacity-100"
					on:click={() => goto('/watch/' + anime.anilistId + '/episode/1')}
				>
					<p>Watch Now</p>
					<div class="flex w-12 justify-center rounded-full bg-secondary/50 p-1">
						<PlayIcon class="fill-white p-1" />
					</div>
				</Button>
				<button
					class="flex items-center space-x-2 rounded-full border-2 border-foreground bg-muted px-4 opacity-75 shadow-2xl transition hover:opacity-100 dark:border-none"
					on:click={() => openModal(anime.anilistId)}
				>
					<span>More Info</span>
					<InfoIcon />
				</button>
			</div>
		</div>
	</div>
	<div
		class="mx-auto mt-8 flex aspect-[460/650] max-h-[650px] w-[75%] max-w-[460px] space-x-4 overflow-hidden rounded-xl border border-primary/75 md:hidden"
	>
		<div
			class="w-full"
			style="background-image: url({anime.image}); background-size: cover; background-position: center;  background-repeat: no-repeat;"
		>
			<div class="flex h-full w-full flex-col justify-end bg-gradient-to-b from-transparent to-black/90">
				<div class="flex flex-col items-center justify-center px-8 text-center">
					<h1 class="xs:text-xs font-bold uppercase sm:text-lg">{anime.title}</h1>
					<p class="flex space-x-4 pb-4 pt-2 text-xs font-extralight opacity-75">
						{genres.join(' • ')}
					</p>
				</div>

				<div class="flex justify-center space-x-4 px-4 pb-8">
					<Button class="h-8 w-[50%] space-x-2 border border-primary/75 py-4 text-sm transition hover:bg-primary/75">
						<PlayIcon class="h-4 w-4" />
						<p>Watch</p>
					</Button>
					<Button
						class="h-8 w-[50%] space-x-2 border bg-black/80 py-4 opacity-100 transition hover:bg-black/80 hover:opacity-75"
						on:click={() => openModal(anime.anilistId)}
					>
						<InfoIcon class="h-4 w-4" />
						<p>More Info</p>
					</Button>
				</div>
			</div>
		</div>
	</div>
{/if}
