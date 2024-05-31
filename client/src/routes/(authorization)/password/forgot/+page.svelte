<script lang="ts">
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
	import { curlNoAuth } from '$lib/api/client';
	import toast from 'svelte-french-toast';
	import Logo from '$lib/assets/icons/aniflix-logo-large.webp?enhanced';
	import Footer from '$lib/components/app/footer/footer.svelte';
	import { goto } from '$app/navigation';

	let email: string;
	let isLoading = false;

	async function handleSubmit() {
		isLoading = true;
		const response: Response = await curlNoAuth(SERVER_URLS.REQUEST_PASSWORD_RESET_PATH + `?email=${email}`, {
			method: 'GET'
		});

		if (response.ok) {
			toast.success('Password reset email sent successfully. Please check your email for further instructions.', {
				duration: 5000,
				position: 'bottom-center',
				style: 'background: #262626; color: #ffffff;'
			});
		}

		isLoading = false;
	}
</script>

<head>
	<title>Reset - Aniflix</title>
	<meta name="description" content="Request password reset for your account." />
	<meta name="keywords" content="AniFlix, Password, Reset" />
</head>

<div class="max-w-screen absolute inset-0 max-h-screen">
	<div class="mx-auto my-auto flex h-[85%] w-[350px] flex-col justify-center space-y-6">
		<div class="px-16">
			<enhanced:img src={Logo} alt="AniFlix Logo" fetchpriority="high" loading="eager" />
		</div>
		<form id="forgot-password" on:submit|preventDefault={handleSubmit}>
			<Card>
				<CardHeader class="space-y-1">
					<CardTitle class="text-2xl">Password Reset</CardTitle>
					<CardDescription>A link will be sent to your email address to reset your password.</CardDescription>
				</CardHeader>
				<CardContent class="my-4">
					<div class="grid gap-2">
						<Label>Email</Label>
						<Input type="email" placeholder="Enter your email.." autocomplete="email" required bind:value={email} />
					</div>
				</CardContent>
				<CardFooter class="my-4 space-x-2">
					<Button form="forgot-password" type="submit" class="w-full" disabled={isLoading || email === ''}>
						{#if isLoading}
							<div
								class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
								role="status"
							/>
						{:else}
							<span>Send mail</span>
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
