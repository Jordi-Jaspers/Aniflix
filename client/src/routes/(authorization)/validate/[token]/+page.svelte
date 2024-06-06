<script lang="ts">
import { onMount } from 'svelte';
import { page } from '$app/stores';
import { validateAccount } from '$lib/api/client';
import { LoadingScreen } from '$lib/components/general/index.js';

onMount(async () => {
	await validateAccount($page.params.token.toString());
});
</script>

<LoadingScreen />
