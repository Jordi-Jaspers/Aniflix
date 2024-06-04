<script lang="ts">
import { Pause, Play, Volume2, VolumeX } from 'lucide-svelte';
import { Button, type Events } from '$lib/components/ui/button';

type $$Events = Events;
export let isPlaying: boolean = false;
</script>

<button
	class="aspect-square h-full w-auto rounded-full border-2 border-card-foreground p-1.5 opacity-60 transition hover:opacity-100 dark:border-[gray]"
	on:click
	on:keydown
>
	{#if !isPlaying}
		<Play />
	{:else}
		<Pause />
	{/if}
</button>
