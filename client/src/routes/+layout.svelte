<script>
	import * as Alert from '$lib/components/ui/alert';
	import { AlertCircle } from 'lucide-svelte';
	import '../app.pcss';
	import { useIsAuthorized } from './../lib/store.ts';

	const isAuthorized = useIsAuthorized();
</script>

<main class="flex h-screen flex-col">
	<slot />
	{#if !isAuthorized}
		<div class="absolute bottom-[5%] right-0 w-screen px-8 lg:w-[50%]">
			<Alert.Root class="bg-gray-200/80" variant="destructive">
				<AlertCircle class="h-4 w-4" />
				<Alert.Title>Error</Alert.Title>
				<Alert.Description>You are unauthorized or your session has expired.</Alert.Description>
			</Alert.Root>
		</div>
	{/if}
</main>
