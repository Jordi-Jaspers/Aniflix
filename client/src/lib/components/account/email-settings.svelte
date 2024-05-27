<script lang="ts">
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card/index.js';
	import { Label } from '$lib/components/ui/label/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import { Button } from '$lib/components/ui/button/index.js';
	import { useUserDetails } from '$lib/components/store/store.js';
	import toast from 'svelte-french-toast';
	import { SERVER_URLS } from '$lib/api/paths';
	import { curl, logout } from '$lib/api/client';
	import Check from 'lucide-svelte/icons/check';
	import { X } from 'lucide-svelte';
	import { writable } from 'svelte/store';

	let isLoading = writable(false);
	let request = writable<UpdateEmailRequest>({ email: '' });
	let emailAvailable = writable(false);

	$: isButtonDisabled = $isLoading || !$request.email || !$emailAvailable;

	async function handleSubmit() {
		isLoading.set(true);
		request.update((req) => {
			req.email = req.email !== '' ? req.email : $useUserDetails.email;
			return req;
		});

		const response: Response = await curl(SERVER_URLS.USER_DETAILS_EMAIL_PATH, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify($request)
		});

		if (response.ok) {
			$useUserDetails.email = $request.email;
			request.set({ email: '' });
			toast.success('Email updated successfully. Check your email for further instructions.', {
				duration: 5000,
				position: 'bottom-center',
				style: 'background: #262626; color: #ffffff;'
			});
			await logout();
		}
		isLoading.set(false);
	}

	let checkingEmail: boolean = writable(false);
	let debounceTimeout: Timer;
	async function isEmailInAlreadyUse() {
		checkingEmail.set(true);
		if (!$request.email) return;

		if (debounceTimeout) {
			clearTimeout(debounceTimeout);
		}

		debounceTimeout = setTimeout(async () => {
			const response: Response = await curl(SERVER_URLS.EMAIL_VALIDATION_PATH, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify($request)
			});

			if (response.ok) {
				const result: boolean = await response.json();
				emailAvailable.set(!result);
			}
			checkingEmail.set(false);
		}, 300);
	}

	$: $request.email && isEmailInAlreadyUse();
</script>

<form id="change-email" on:submit|preventDefault={handleSubmit}>
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Email preferences</CardTitle>
			<CardDescription>
				This email will be used for account-related notifications and can also be used for password resets. Changing this will log you out
				on any device and send you a mail to verify the new email address.
			</CardDescription>
		</CardHeader>
		<CardContent class="space-y-4">
			<div class="relative grid gap-2">
				<Label>Public email</Label>
				<Input type="email" placeholder={$useUserDetails.email} bind:value={$request.email} class="pr-10" autocomplete="email" />
				{#if $request.email}
					{#if $checkingEmail}
						<div class="absolute right-2 top-[50%]">
							<div
								class="inline-block h-6 w-6 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
								role="status"
							/>
						</div>
					{:else if $emailAvailable}
						<Check class="absolute right-2 top-[50%] text-green-500" />
					{:else}
						<X class="absolute right-2 top-[50%] text-red-500" />
					{/if}
				{/if}
			</div>
		</CardContent>
		<CardFooter class="my-4">
			<Button form="change-email" type="submit" class="w-fit" disabled={isButtonDisabled}>
				{#if $isLoading}
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
						role="status"
					/>
				{:else}
					<span>Change Email</span>
				{/if}
			</Button>
		</CardFooter>
	</Card>
</form>
