<script lang="ts">
	import '../app.pcss';
	import { Toaster } from 'svelte-french-toast';
	import { ModeWatcher } from 'mode-watcher';
</script>

<main>
	<ModeWatcher defaultMode={'dark'} />
	<slot class="bg-[#1a1920]" />
	<Toaster />
</main>
