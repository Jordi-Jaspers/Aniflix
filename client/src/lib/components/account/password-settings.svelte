<script lang="ts">
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card/index.js';
	import { Label } from '$lib/components/ui/label/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import { Button } from '$lib/components/ui/button/index.js';
	import { toast } from 'svelte-sonner';
	import { SERVER_URLS } from '$lib/api/paths';
	import { curl } from '$lib/api/client';
	import { PasswordMeter } from '$lib/components/general';
	import { writable } from 'svelte/store';
	import type { Writable } from 'svelte/store';
	import { useUserDetails } from '$lib/components/store/store';

	let isLoading: Writable<boolean> = writable(false);
	let isFormValid: Writable<boolean> = writable(false);
	let form: Writable<UpdatePasswordRequest> = writable<UpdatePasswordRequest>({ oldPassword: '', newPassword: '', confirmPassword: '' });

	async function handleSubmit() {
		$isLoading = true;
		const response: Response = await curl(SERVER_URLS.UPDATE_PASSWORD_PATH, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify($form)
		});

		if (response.ok) {
			$form = { oldPassword: '', newPassword: '', confirmPassword: '' };
			toast.success('Password updated successfully');
		}
		$isLoading = false;
	}
</script>

<form id="update-password" on:submit|preventDefault={handleSubmit} data-bitwarden-watching="1">
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Update Password</CardTitle>
			<CardDescription
				>Update your current password. Make sure it's at least 12 characters OR at least 8 characters including a number, a lowercase
				letter, an uppercase and a special character
			</CardDescription>
		</CardHeader>
		<CardContent class="space-y-4">
			<div class="hidden gap-2">
				<Label>email</Label>
				<Input type="email" required bind:placeholder={$useUserDetails.email} autocomplete="email" />
			</div>
			<div class="grid gap-2">
				<Label>Old Password</Label>
				<Input type="password" required bind:value={$form.oldPassword} autocomplete="current-password" />
			</div>
			<div class="grid gap-2">
				<Label>New Password</Label>
				<Input type="password" required bind:value={$form.newPassword} autocomplete="new-password" />
			</div>
			<div class="grid gap-2">
				<Label>Confirm New Password</Label>
				<Input type="password" required bind:value={$form.confirmPassword} autocomplete="new-password" />
			</div>
			<PasswordMeter bind:password={$form.newPassword} bind:confirmation={$form.confirmPassword} bind:isValid={$isFormValid} />
		</CardContent>
		<CardFooter class="my-4 space-x-2">
			<Button form="update-password" type="submit" class="w-full" disabled={$isLoading || !$isFormValid}>
				{#if $isLoading}
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
						role="status"
					/>
				{:else}
					<span>Update Password</span>
				{/if}
			</Button>
		</CardFooter>
	</Card>
</form>
