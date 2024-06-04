<script lang="ts">
import { InfoIcon } from 'lucide-svelte';
import { openModal } from '$lib/api/modal-util';
import { getAnilistId } from '$lib/components/store/anime-context-store';
import type { Writable } from 'svelte/store';

let anilistId: Writable<number> = getAnilistId();
</script>

<button on:click={() => openModal($anilistId)}>
	<InfoIcon class="aspect-square h-full w-auto rounded-full bg-card/60 transition hover:border-primary/60 hover:text-primary/60" />
</button>
