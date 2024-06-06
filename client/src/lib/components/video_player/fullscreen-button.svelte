<script lang="ts">
import { Maximize } from 'lucide-svelte';
import screenfull from 'screenfull';

function toggleFullScreen() {
	return screenfull.isFullscreen ? screenfull.exit() : screenfull.request(document.querySelector('.svelte-player') ?? undefined);
}
</script>

<button aria-label="Fullscreen" class="h-full w-full" on:click={toggleFullScreen}>
	<Maximize class="aspect-square h-full w-auto hover:scale-125" />
</button>
