<script>
import Logo from '$lib/assets/icons/aniflix-logo-large.webp?enhanced';
import LoginBackground from '$lib/assets/images/aniflix-login.webp?enhanced';
import LoginCard from '$lib/components/login/login-card.svelte';
import RegisterCard from '$lib/components/login/register-card.svelte';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '$lib/components/ui/tabs';
import { Badge } from '$lib/components/ui/badge/index';
</script>

<head>
	<title>Login - Aniflix</title>
	<meta name="description" content="Login or Register to AniFlix.stream" />
	<meta name="keywords" content="AniFlix, Login, Register, Authentication" />
</head>

<div class="container relative grid h-screen flex-col items-center justify-center lg:max-w-none lg:grid-cols-2 lg:px-0">
	<div class="relative hidden h-full flex-col bg-muted p-10 text-white dark:border-r lg:flex">
		<div class="absolute inset-0 min-h-screen min-w-full">
			<enhanced:img src={LoginBackground} alt="Login Background" class="h-full overflow-hidden object-cover brightness-[0.35]" />
		</div>

		<div class="relative z-20 flex items-center space-x-2 text-lg font-medium">
			Aniflix.stream
			{#if import.meta.env.VITE_ENV === 'test'}
				<Badge variant="outline" class="my-3 bg-blue-600">TST</Badge>
			{/if}

			{#if import.meta.env.VITE_ENV === 'development'}
				<Badge variant="outline" class="my-3 bg-green-600">DEV</Badge>
			{/if}
		</div>

		<div class="relative z-20 mt-auto">
			<blockquote class="space-y-2">
				{#if import.meta.env.VITE_ENV !== 'production'}
					<p class="text-lg">&ldquo;The website is currently in a testing stage. 🔐 &rdquo;</p>
					<footer class="text-sm">The Developer</footer>
				{:else}
					<p class="text-lg">&ldquo;I'll find a good quote to put here.&rdquo;</p>
					<footer class="text-sm">The Developer</footer>
				{/if}
			</blockquote>
		</div>
	</div>
	<div class="lg:p-8">
		<div class="mx-auto flex w-[350px] flex-col justify-center space-y-6">
			<div class="px-16">
				<enhanced:img src={Logo} alt="AniFlix Logo" fetchpriority="high" loading="eager" />
			</div>
			<Tabs defaultValue="login">
				<TabsList class="w-full">
					<TabsTrigger class="w-[50%] text-center" value="login">Login</TabsTrigger>
					<TabsTrigger class="w-[50%] text-center" value="register">Register</TabsTrigger>
				</TabsList>
				<TabsContent value="login">
					<LoginCard />
				</TabsContent>
				<TabsContent value="register">
					<RegisterCard />
				</TabsContent>
			</Tabs>
			<p class="px-8 text-center text-sm text-muted-foreground">
				By clicking continue, you agree to our{' '}
				<a href="/terms" class="underline underline-offset-4 hover:text-primary"> Terms of Service </a>{' '}
				and{' '}
				<a href="/privacy" class="underline underline-offset-4 hover:text-primary"> Privacy Policy </a>
				.
			</p>
		</div>
	</div>
</div>
