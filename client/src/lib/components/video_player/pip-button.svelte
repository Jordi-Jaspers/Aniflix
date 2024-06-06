<script lang="ts">
import { PictureInPicture2 } from 'lucide-svelte';
import Device from 'svelte-device-info';

export let pip: boolean;

function togglePiP() {
	pip = !pip;
}
</script>

{#if !Device.isMobile && !Device.isTablet}
	<button aria-label="PiP" class="h-full w-full" on:click={togglePiP}>
		<PictureInPicture2 class="aspect-square h-full w-auto hover:scale-125" />
	</button>
{/if}
