<script lang="ts">
import { Volume2, VolumeX } from 'lucide-svelte';
import { Button, type Events } from '$lib/components/ui/button';

type $$Events = Events;
export let isMuted: boolean = false;
</script>

<button
	class="aspect-square h-full w-auto rounded-full border-2 border-card-foreground p-1.5 opacity-60 transition hover:opacity-100 dark:border-[gray]"
	on:click={() => (isMuted = !isMuted)}
>
	{#if isMuted}
		<VolumeX />
	{:else}
		<Volume2 />
	{/if}
</button>
