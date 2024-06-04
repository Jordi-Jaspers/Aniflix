<script lang="ts">
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
	import { curlNoAuth } from '$lib/api/client';
	import Logo from '$lib/assets/icons/aniflix-logo-large.webp?enhanced';
	import Footer from '$lib/components/app/footer/footer.svelte';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { PasswordMeter } from '$lib/components/general/index.js';
	import { writable } from 'svelte/store';
	import type { Writable } from 'svelte/store';
	import { toast } from 'svelte-sonner';

	let isLoading: Writable<boolean> = writable(false);
	let isFormValid: Writable<boolean> = writable(false);
	let form: Writable<ForgotPasswordRequest> = writable<ForgotPasswordRequest>({ token: '', newPassword: '', confirmPassword: '' });

	async function handleSubmit() {
		$isLoading = true;
		$form.token = $page.params.token.toString();
		const response = await curlNoAuth(SERVER_URLS.PUBLIC_PASSWORD_RESET_PATH, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify($form)
		});

		if (response.ok) {
			$form = { token: '', newPassword: '', confirmPassword: '' };
			toast.success('Password reset email sent successfully. Please check your email for further instructions.');
			await goto(CLIENT_URLS.LOGIN_URL);
		}
		$isLoading = false;
	}
</script>

<head>
	<title>Change Password - Aniflix</title>
	<meta name="description" content="Change your password on Aniflix" />
	<meta name="keywords" content="Aniflix, Change Password, Reset Password" />
</head>

<div class="max-w-screen absolute inset-0 max-h-screen">
	<div class="mx-auto my-auto flex h-[85%] w-[350px] flex-col justify-center space-y-6">
		<div class="px-16">
			<enhanced:img src={Logo} alt="AniFlix Logo" fetchpriority="high" loading="eager" />
		</div>
		<form id="change-password" on:submit|preventDefault={handleSubmit}>
			<Card>
				<CardHeader class="space-y-1">
					<CardTitle class="text-2xl">Reset Your Password</CardTitle>
					<CardDescription>Enter your new password below.</CardDescription>
				</CardHeader>
				<CardContent class="my-4 space-y-4">
					<div class="grid gap-2">
						<Label>New Password</Label>
						<Input type="password" required bind:value={$form.newPassword} />
					</div>
					<div class="grid gap-2">
						<Label>Confirm New Password</Label>
						<Input type="password" required bind:value={$form.confirmPassword} />
					</div>
					<PasswordMeter bind:password={$form.newPassword} bind:confirmation={$form.confirmPassword} bind:isValid={$isFormValid} />
				</CardContent>
				<CardFooter class="my-4 space-x-2">
					<Button form="change-password" type="submit" class="w-full" disabled={$isLoading || !$isFormValid}>
						{#if $isLoading}
							<div
								class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
								role="status"
							/>
						{:else}
							<span>Change Password</span>
						{/if}
					</Button>
					<Button class="w-full" on:click={() => goto(CLIENT_URLS.LOGIN_URL)}>
						<span>Back to Login</span>
					</Button>
				</CardFooter>
			</Card>
		</form>
	</div>
</div>

<div class="absolute bottom-0 w-full">
	<Footer />
</div>
