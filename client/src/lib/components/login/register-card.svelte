<script lang="ts">
	import Socials from '$lib/components/login/socials.svelte';
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { ShieldAlert, ShieldCheck } from 'lucide-svelte';
	import { register } from '$lib/api/client';
	import { PasswordMeter } from '$lib/components/general';

	let formData: RegisterRequest = {
		email: '',
		firstName: '',
		lastName: '',
		password: '',
		passwordConfirmation: ''
	};

	let errorMessage: string | undefined;
	let registerResponse: RegisterResponse | undefined;
	let isLoading = false;

	async function handleSubmit() {
		isLoading = true;
		const response: AuthorizeResponse | string = await register(formData);
		if (typeof response === 'string') {
			registerResponse = undefined;
			errorMessage = response;
		} else {
			registerResponse = response;
			errorMessage = undefined;
		}
		isLoading = false;
	}
</script>

<form id="register" on:submit|preventDefault={handleSubmit}>
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Create an account</CardTitle>
			<CardDescription>Enter your email below to create your account</CardDescription>
		</CardHeader>
		<CardContent class="grid gap-4">
			<div class="grid gap-2">
				<Label>Email</Label>
				<Input id="email" type="email" placeholder="johndoe@example.com" autocomplete="username" required bind:value={formData.email} />
			</div>
			<div class="grid gap-2">
				<Label>First Name</Label>
				<Input id="firstName" type="firstName" placeholder="John" required bind:value={formData.firstName} />
			</div>
			<div class="grid gap-2">
				<Label>Last Name</Label>
				<Input id="lastName" type="lastName" placeholder="Doe" required bind:value={formData.lastName} />
			</div>
			<div class="grid gap-2">
				<Label>Password</Label>
				<Input id="password" type="password" autocomplete="new-password" placeholder="*****" required bind:value={formData.password} />
			</div>
			<div class="grid gap-2">
				<Label>Confirm Password</Label>
				<Input
					id="passwordConfirmation"
					type="password"
					autocomplete="new-password"
					placeholder="*****"
					required
					bind:value={formData.passwordConfirmation}
				/>
			</div>

			<PasswordMeter bind:password={formData.password} />

			{#if errorMessage && registerResponse === undefined}
				<div class="mx-1 flex flex-row content-center justify-center space-x-2 text-sm text-red-500">
					<ShieldAlert class="m-2 w-[10%]" />
					<span class="w-[90%]">
						{errorMessage}
					</span>
				</div>
			{/if}

			{#if registerResponse && !registerResponse.validated && errorMessage === undefined}
				<div class="space-x- mx-1 flex flex-row content-center justify-center text-sm text-green-500">
					<ShieldCheck class="m-2 w-[10%]" />
					<span class="w-[90%]">
						You have successfully registered. Please check your mailbox to verify your email address.
						<button type="button" class="underline"> Resend email </button>
					</span>
				</div>
			{/if}

			<div class="relative my-2">
				<div class="absolute inset-0 flex items-center">
					<span class="w-full border-t" />
				</div>
				<div class="relative flex justify-center text-xs uppercase">
					<span class="bg-background px-2 text-muted-foreground">Or continue with</span>
				</div>
			</div>
			<Socials />
		</CardContent>
		<CardFooter>
			<Button form="register" type="submit" class="w-full" disabled={isLoading}>
				{#if isLoading}
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
						role="status"
					/>
				{:else}
					<span>Create Account</span>
				{/if}
			</Button>
		</CardFooter>
	</Card>
</form>
