<script lang="ts">
	import { authorize } from '$lib/components/api/authorization';
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { AlertCircle } from 'lucide-svelte';

	let formData: LoginInput = {
		email: '',
		password: ''
	};

	let errorMessage: string;
	const onSubmit = () => {
		authorize(formData)
			.then((response: AuthorizeResponse) => {
				localStorage.setItem('ANIFLIX_ACCESS_TOKEN', response.accessToken);
				localStorage.setItem('ANIFLIX_REFRESH_TOKEN', response.refreshToken);
				window.location.href = '/browse';
			})
			.catch((message) => {
				errorMessage = message;
			});
	};
</script>

<form id="login" on:submit|preventDefault={onSubmit}>
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Sign In</CardTitle>
			<CardDescription>Enter your email below and start watching</CardDescription>
		</CardHeader>
		<CardContent class="grid gap-4">
			<div class="grid gap-2">
				<Label>Email</Label>
				<Input id="email" type="email" placeholder="johndoe@example.com" required bind:value={formData.email} />
			</div>
			<div class="grid gap-2">
				<Label>Password</Label>
				<Input id="password" type="password" placeholder="Password" required bind:value={formData.password} />
			</div>

			{#if errorMessage}
				<div class="mx-1 flex flex-row content-center justify-center space-x-2 text-red-500">
					<AlertCircle />
					<span class="text-sm">{errorMessage}</span>
				</div>
			{/if}

			<div class="relative">
				<div class="absolute inset-0 flex items-center">
					<span class="w-full border-t" />
				</div>
				<div class="relative flex justify-center text-xs uppercase">
					<span class="bg-background px-2 text-muted-foreground">Or continue with</span>
				</div>
			</div>
			<div class="grid grid-cols-2 gap-6">
				<Button variant="outline">Github</Button>
				<Button variant="outline">Google</Button>
			</div>
		</CardContent>
		<CardFooter>
			<Button form="login" type="submit" class="w-full">Create account</Button>
		</CardFooter>
	</Card>
</form>
