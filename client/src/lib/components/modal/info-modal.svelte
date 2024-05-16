<script lang="ts">
	import { useModalInfo, useShowInfoModal } from '$lib/components/store/store';
	import { PlayIcon, StarIcon, X } from 'lucide-svelte';
	import SveltePlayer from 'svelte-player';
	import type { RecursivePartial } from 'svelte-player/dist/players/utility.types';
	import type { PlayerConfig } from 'svelte-player/dist/players/types';
	import { Button } from '$lib/components/ui/button';
	import { LibraryButton, LikeButton, SpeakerButton } from '$lib/components/general/index.js';
	import { Badge } from '$lib/components/ui/badge';
	import { closeModal } from '$lib/api/util';
	import { EpisodeList, RecommendationCards } from '$lib/components/browse';
	import { Content, List, Root, Trigger } from '$lib/components/ui/tabs';
	import { setAnime } from '$lib/components/store/anime-context-store';
	import { goto } from '$app/navigation';

	let isPlaying: boolean = false;
	let isMuted: boolean = false;

	let lastEpisode: number = $useModalInfo.lastSeenEpisode !== 0 ? $useModalInfo.lastSeenEpisode : 1;
	const opts: RecursivePartial<PlayerConfig> = {
		youtube: {
			playerVars: {
				autoplay: 1,
				controls: 0,
				https: 1,
				wmode: 'opaque',
				disablekb: 1
			}
		}
	};

	function handleEscape(e: KeyboardEvent) {
		if (e.key === 'Escape') {
			closeModal();
		}
	}

	useModalInfo.subscribe((value) => {
		if (value) {
			setAnime(value);
		}
	});
</script>

{#if $useShowInfoModal}
	<button on:click={() => closeModal()} class="fixed inset-0 !z-[100] h-full w-full overflow-y-auto scroll-smooth backdrop-brightness-50" />
	<div
		role="presentation"
		on:close={() => closeModal()}
		on:keypress={handleEscape}
		class="fixed inset-0 !z-[1000] mx-auto h-auto w-full overflow-y-scroll overscroll-auto bg-background md:top-8 md:max-w-[90%] md:rounded-t-md lg:max-w-4xl"
	>
		<button
			class="onclick absolute right-0 top-0 !z-40 m-[1em] flex h-9 w-9 items-center justify-center rounded-full bg-[#1a1920]/80 text-white hover:bg-[#1a1920]"
			on:click={() => closeModal()}
		>
			<X class="h-6 w-6" />
		</button>
		<div class="relative !z-[-100] aspect-video w-full">
			<div class="absolute right-0 aspect-video h-[100%] bg-gradient-to-b from-transparent to-background" />
			<img
				class="brightness-85 aspect-video w-full h-auto object-cover object-center {isPlaying && 'hidden'}"
				src={$useModalInfo.cover}
				alt="thumbnail"
			/>
			{#if import.meta.env.VITE_ENV !== 'development'}
				<div class="brightness-85 h-full {isPlaying ? '' : 'hidden'}">
					<SveltePlayer
						url="https://www.youtube.com/watch?v={$useModalInfo.trailer}"
						config={opts}
						height="100%"
						width="100%"
						bind:muted={isMuted}
						on:play={() => (isPlaying = true)}
						on:pause={() => (isPlaying = false)}
						on:ended={() => (isPlaying = false)}
					>
						<div slot="play-icon" />
					</SveltePlayer>
				</div>
			{/if}
			<div class="absolute bottom-10 flex w-full items-center justify-between px-10">
				<div class="flex items-center space-x-2">
					<Button
						class="space-x-4 rounded-full pl-4 pr-1 opacity-80 transition hover:opacity-100"
						on:click={() => goto('/watch/' + $useModalInfo.anilistId + '/episode/' + lastEpisode)}
					>
						<p>Watch Now</p>
						<div class="flex w-12 justify-center rounded-full bg-secondary/50 p-1">
							<PlayIcon class="fill-white p-1" />
						</div>
					</Button>

					<div class="flex h-10 justify-center space-x-2">
						<LikeButton />
						<LibraryButton />
					</div>
				</div>
				{#if isPlaying}
					<SpeakerButton {isMuted} on:click={() => (isMuted = !isMuted)} />
				{/if}
			</div>
		</div>
		<div class="z-10 w-full flex-col items-center justify-center space-y-6 px-10 text-lg">
			<div class="space-y-2">
				<h3 class="flex items-center">
					{$useModalInfo.title
						.split(' ')
						.map((word) => word.charAt(0).toUpperCase() + word.slice(1))
						.join(' ')
						.split('-')
						.map((word) => word.charAt(0).toUpperCase() + word.slice(1))
						.join('-')}
					<Badge variant="outline" class="ml-4 bg-blue-500">
						{#if $useModalInfo.subbed}
							SUB{:else}
							DUB{/if}
					</Badge>
				</h3>

				<div class="flex h-[1.2vw] w-[1.2] items-center text-[1.1vw] text-sm text-muted-foreground">
					<div class="flex items-center space-x-1">
						<p>{$useModalInfo.rating / 10}</p>
						<StarIcon class="ml-0.5 h-3 w-fit fill-amber-300 text-amber-300" />
					</div>

					<span class="mx-2"> | </span>
					<span> {$useModalInfo.releaseYear} </span>
					<span class="mx-2"> | </span>
					<span> {$useModalInfo.totalEpisodes} Episodes </span>
					<p class="mx-2 flex h-4 items-center justify-center rounded border px-1.5 text-xs">HD</p>
				</div>
			</div>

			<div class="flex w-full flex-col gap-x-10 gap-y-4 font-light md:flex-row">
				<article class="prose md:w-[85%] text-justify text-sm font-extralight leading-4 md:leading-7">
					{@html $useModalInfo.description.replace(/\(Source:.*\)/, '')}
				</article>
				<div class="flex flex-col space-y-4 text-sm">
					<p>
						<span class="text-muted-foreground">Genres:</span>{' '}
						{$useModalInfo.genres
							.filter((genre) => genre !== 'UNKNOWN')
							.map((genre) => genre.charAt(0) + genre.slice(1).toLowerCase())
							.join(', ')}
					</p>
					<p>
						<span class="text-muted-foreground">Media:</span>{' '}
						{$useModalInfo.mediaType}
					</p>
					<p>
						<span class="text-muted-foreground">Status:</span>{' '}
						{$useModalInfo.status.charAt(0) + $useModalInfo.status.slice(1).toLowerCase()}
					</p>
					<p>
						<span class="text-muted-foreground">Watch Status:</span>{' '}
						{$useModalInfo.watchStatus}
					</p>
					<p>
						<span class="text-muted-foreground">Last Seen:</span>{' '}
						{#if $useModalInfo.lastSeenEpisode === 0}
							-
						{:else}
							Episode {$useModalInfo.lastSeenEpisode}
						{/if}
					</p>
				</div>
			</div>
			<Root value="Episodes" class="py-8">
				<List class="space-x-4 bg-transparent">
					<Trigger
						value="Episodes"
						class="rounded-none border-primary p-0 text-xl data-[state=active]:border-b-2 data-[state=active]:bg-transparent"
					>
						Episodes
					</Trigger>
					<Trigger
						value="Recommended"
						class="rounded-none border-primary p-0 text-xl data-[state=active]:border-b-2 data-[state=active]:bg-transparent"
					>
						Recommended
					</Trigger>
				</List>
				<Content value="Episodes">
					<EpisodeList anilistId={$useModalInfo.anilistId} />
				</Content>
				<Content value="Recommended">
					<RecommendationCards anilistId={$useModalInfo.anilistId} />
				</Content>
			</Root>
		</div>
	</div>
{/if}
