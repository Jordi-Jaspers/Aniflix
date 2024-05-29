<script lang="ts">
	import { Button } from '$lib/components/ui/button/index.js';
	import { HeartPulse } from 'lucide-svelte';
	import * as AlertDialog from '$lib/components/ui/alert-dialog/index.js';
	import { SERVER_BASE_URL } from '$lib/api/paths';
	import { writable } from 'svelte/store';

	let providers = writable([
		{
			name: 'Aniflix',
			healthEndpoint: SERVER_BASE_URL + '/actuator/health',
			status: 'Online'
		},
		{
			name: 'Anilist',
			healthEndpoint: 'https://anilist.co/',
			status: 'Online'
		},
		{
			name: 'MyAnimeList',
			healthEndpoint: 'https://myanimelist.net/',
			status: 'Online'
		},
		{
			name: 'GogoAnime',
			healthEndpoint: 'https://anitaku.so/',
			status: 'Online'
		},
		{
			name: 'Zoro',
			healthEndpoint: 'https://hianime.to/',
			status: 'Online'
		}
	]);

	async function pingProviders() {
		const updatedProviders = $providers.map(async (provider) => {
			try {
				await fetch(provider.healthEndpoint, { method: 'HEAD', mode: 'no-cors' });
				provider.status = 'Online';
			} catch (error) {
				provider.status = 'Offline';
			}
			return provider;
		});
		providers.set(await Promise.all(updatedProviders));
	}
</script>

<AlertDialog.Root closeOnOutsideClick={true}>
	<AlertDialog.Trigger asChild let:builder>
		<Button builders={[builder]} variant="ghost" size="icon" class="rounded-[0.75rem]" on:click={() => pingProviders()}>
			<HeartPulse />
		</Button>
	</AlertDialog.Trigger>
	<AlertDialog.Content>
		<AlertDialog.Header>
			<AlertDialog.Title class="text-start">Provider Status</AlertDialog.Title>
			<AlertDialog.Description class="text-start">
				Underneath is a list of all the providers that are currently being monitored, because this application depends on it. If you have
				any issues with a provider, please report it to the AniFlix team.
			</AlertDialog.Description>
			<ul class="mx-auto w-[75%] space-y-4 py-4">
				{#each $providers as provider}
					<li class="flex justify-between">
						<span>{provider.name}</span>
						<div>
							<span class="text-[0.75rem] font-bold {provider.status === 'Online' ? 'text-green-500' : 'text-red-500'}">
								{provider.status}
							</span>
							<span
								class="ml-2 inline-block h-2 w-2 animate-pulse rounded-full {provider.status === 'Online' ? 'bg-green-500' : 'bg-red-500'}"
							/>
						</div>
					</li>
				{/each}
			</ul>
		</AlertDialog.Header>
		<AlertDialog.Footer>
			<AlertDialog.Action>Close</AlertDialog.Action>
		</AlertDialog.Footer>
	</AlertDialog.Content>
</AlertDialog.Root>
