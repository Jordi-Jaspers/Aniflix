<script lang="ts">
	import { enhance } from '$app/forms';
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
</script>

<Card>
	<form
		method="POST"
		action="{import.meta.env.VITE_SERVER_BASE_URL}/api/login"
		use:enhance={({ formElement, formData: LoginInput }) => {
			console.log(FormData.email);
		}}
	>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Sign In</CardTitle>
			<CardDescription>Enter your email below and start watching</CardDescription>
		</CardHeader>
		<CardContent class="grid gap-4">
			<div class="grid gap-2">
				<Label>Email</Label>
				<Input id="email" type="email" placeholder="johndoe@example.com" required />
			</div>
			<div class="grid gap-2">
				<Label>Password</Label>
				<Input id="password" type="password" required />
			</div>
			<div class="relative">
				<div class="absolute inset-0 flex items-center">
					<span class="w-full border-t" />
				</div>
				<div class="relative flex justify-center text-xs uppercase">
					<span class="bg-background text-muted-foreground px-2">Or continue with</span>
				</div>
			</div>
			<div class="grid grid-cols-2 gap-6">
				<Button variant="outline">Github</Button>
				<Button variant="outline">Google</Button>
			</div>
		</CardContent>
		<CardFooter>
			<Button type="submit" class="w-full">Create account</Button>
		</CardFooter>
	</form>
</Card>
