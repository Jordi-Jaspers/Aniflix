<script lang="ts">
	import ErrorMessage from '$lib/components/general/error-message.svelte';
	import {useAuthenticated, useHasError} from '$lib/store';
	import {onMount} from 'svelte';
	import '../app.pcss';

	let isAuthenticated: boolean;
	let hasError: boolean;

	onMount(() => {
		useHasError.subscribe((value) => {
			hasError = value;
			if (hasError) {
				setTimeout(() => {
					hasError = false;
				}, 5000);
			}
		});
		useAuthenticated.subscribe((value) => {
			isAuthenticated = value;
		});
	});
</script>

<main>
	<slot class="bg-[#1a1920]"/>
	{#if !isAuthenticated && hasError}
		<div class="fade absolute bottom-[5%] right-0 w-screen px-8 transition lg:w-[50%]">
			<ErrorMessage isVisible={hasError} error="You are unauthorized or your session has expired."/>
		</div>
	{/if}
</main>
