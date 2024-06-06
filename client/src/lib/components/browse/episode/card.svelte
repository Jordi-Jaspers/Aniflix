<script lang="ts">
import { InfoButton } from '$lib/components/general';
import { setAnilistId } from '$lib/components/store/anime-context-store';
import { goto } from '$app/navigation';
import { PlayIcon } from 'lucide-svelte';

let hovering: boolean = false;

export let episode: EpisodeResponse;
setAnilistId(episode.anilistId);
</script>

<div class="flex h-full w-48 flex-col overflow-hidden rounded-t-[0.75rem] md:w-56">
	<div class="relative aspect-[420/600] h-full w-auto bg-card-accent opacity-100 transition-all hover:opacity-75">
		<button
			class="absolute flex h-full w-full items-center justify-center bg-gradient-to-b dark:from-transparent dark:to-card-accent dark:lg:to-[99%]"
			on:click={() => goto('/watch/' + episode.anilistId + '/episode/' + episode.episodeNumber)}
			on:mouseover={() => (hovering = true)}
			on:mouseleave={() => (hovering = false)}
			on:focus={() => (hovering = true)}
		>
			<PlayIcon
				class="h-10 w-10 fill-black text-black opacity-0 dark:fill-white dark:text-white {hovering && 'opacity-100'} transition-opacity"
			/>
		</button>
		<img
			class="mb-2 h-full w-full cursor-pointer bg-center bg-no-repeat"
			src={episode.image}
			alt={episode.title}
			width={420}
			height={600}
		/>
	</div>

	<div class="flex w-full bg-card-accent pb-4">
		<div class="flex h-full w-full space-x-2 object-center px-[4%]">
			<div class="max-h-full w-full space-y-1 py-2">
				<p class="whitespace-nowrap text-xs font-light text-muted-foreground">
					Episode {episode.episodeNumber}
				</p>
				<div class="flex items-center justify-between space-x-2">
					<h4 class="h-[2.5rem] overflow-hidden text-sm">
						{episode.title
							.toLowerCase()
							.split(' ')
							.map((s) => s.charAt(0).toUpperCase() + s.substring(1))
							.join(' ')}
					</h4>
					<div class="flex h-6 w-6 justify-center">
						<InfoButton />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
