<script lang="ts">
import { LibraryButton } from '$lib/components/general';
import { openModal } from '$lib/api/modal-util';
import { setAnime } from '$lib/components/store/anime-context-store';

export let anime: AnimeResponse;
setAnime(anime);
</script>

<div class="relative aspect-[420/600] h-full w-auto bg-card-accent opacity-100 transition-all hover:opacity-75">
	<button
		class="absolute flex h-full w-full items-center justify-center bg-gradient-to-b dark:from-transparent dark:to-card-accent dark:lg:to-[99%]"
		on:click={() => openModal(anime.anilistId)}
	/>
	<img class="mb-2 h-full w-full cursor-pointer bg-center bg-no-repeat" src={anime.image} alt={anime.title} width={420} height={600} />
</div>

<div class="flex w-full bg-card-accent py-2 pb-4">
	<div class="flex h-full w-full space-x-2 object-center px-[4%]">
		<div class="max-h-full w-full space-y-1">
			<p class="whitespace-nowrap text-xs font-light text-muted-foreground">
				{anime.totalEpisodes} Episodes - {anime.releaseYear ? anime.releaseYear : new Date().getFullYear()}
			</p>
			<div class="flex items-center justify-between space-x-2">
				<h4 class="h-[2.5rem] overflow-hidden text-sm">
					{anime.title
						.toLowerCase()
						.split(' ')
						.map((s) => s.charAt(0).toUpperCase() + s.substring(1))
						.join(' ')}
				</h4>
				<div class="flex h-8 w-8 justify-center">
					<LibraryButton />
				</div>
			</div>
		</div>
	</div>
</div>
