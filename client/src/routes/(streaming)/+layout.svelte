<script lang="ts">
	import { goto } from '$app/navigation';
	import { useHasAuthError } from '$lib/components/store/store';
	import { isUserAuthenticated } from '$lib/api/client';
	import { CLIENT_URLS } from '$lib/api/paths';
	import { onMount } from 'svelte';

	let isAuthenticated: boolean;
	onMount(async () => {
		isAuthenticated = await isUserAuthenticated();
		if (!isAuthenticated) {
			goto(CLIENT_URLS.LOGIN_URL);
			$useHasAuthError = true;
		}
	});
</script>

{#if isAuthenticated}
	<main class="h-screen w-screen overflow-hidden bg-black">
		<slot />
	</main>
{/if}
