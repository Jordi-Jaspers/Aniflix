<script lang="ts">
import { goto } from '$app/navigation';
import Socials from '$lib/components/login/socials.svelte';
import { Button } from '$lib/components/ui/button';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
import { Input } from '$lib/components/ui/input';
import { Label } from '$lib/components/ui/label';
import { ShieldAlert, ShieldEllipsis } from 'lucide-svelte';
import { CLIENT_URLS } from '$lib/api/paths';
import { authorize } from '$lib/api/client';

let formData: LoginRequest = {
	email: '',
	password: ''
};

let errorMessage: string | undefined;
let authorizeResponse: AuthorizeResponse | undefined;
let isLoading = false;

async function handleSubmit() {
	isLoading = true;
	const response: AuthorizeResponse | string = await authorize(formData);
	if (typeof response === 'string') {
		errorMessage = response;
	} else {
		errorMessage = undefined;
		authorizeResponse = response;
		if (response.validated) {
			goto(CLIENT_URLS.BROWSE_URL);
		}
	}
	isLoading = false;
}
</script>

<form id="login" on:submit|preventDefault={handleSubmit}>
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Sign In</CardTitle>
			<CardDescription>Enter your email below and start watching</CardDescription>
		</CardHeader>
		<CardContent class="grid gap-4">
			<div class="grid gap-2">
				<Label>Email</Label>
				<Input type="email" placeholder="johndoe@example.com" autocomplete="username" required bind:value={formData.email} />
			</div>
			<div class="grid gap-2">
				<div class="flex flex-row items-center justify-between">
					<Label>Password</Label>
					<a href={CLIENT_URLS.FORGOT_PASSWORD_URL} class="text-sm text-blue-500 hover:underline">Forgot password?</a>
				</div>
				<Input type="password" placeholder="Password" autocomplete="current-password" required bind:value={formData.password} />
			</div>

			{#if errorMessage && authorizeResponse === undefined}
				<div class="mx-1 flex flex-row content-center justify-center space-x-2 text-sm text-red-500">
					<ShieldAlert class="m-2 w-[10%]" />
					<span class="w-[90%]">
						{errorMessage}
					</span>
				</div>
			{/if}

			{#if authorizeResponse && !authorizeResponse.validated && errorMessage === undefined}
				<div class="mx-1 flex flex-row content-center justify-center space-x-2 text-sm text-orange-500">
					<ShieldEllipsis class="m-2 w-[10%]" />
					<span class="w-[90%]">
						Your account has not been validated. Please check your email for a validation link.
						<button type="button" class="underline"> Resend email </button>
					</span>
				</div>
			{/if}

			<div class="relative">
				<div class="absolute inset-0 flex items-center">
					<span class="w-full border-t" />
				</div>
				<div class="relative flex justify-center text-xs uppercase">
					<span class="bg-background px-2 text-muted-foreground"> Or continue with </span>
				</div>
			</div>

			<Socials />
		</CardContent>
		<CardFooter>
			<Button form="login" type="submit" class="w-full" disabled={isLoading}>
				{#if isLoading}
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
						role="status"
					/>
				{:else}
					<span>Log in</span>
				{/if}
			</Button>
		</CardFooter>
	</Card>
</form>
