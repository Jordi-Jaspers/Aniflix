<script lang="ts">
	import { LoadingScreen } from '$lib/components/general';
	import { EmailSettings, PasswordSettings, ProfileSettings } from '$lib/components/account/index.js';

	let isLoading: boolean;
</script>

<svelte:head>
	<title>Profile - Aniflix</title>
	<meta name="description" content="Your personal profile on Aniflix" />
	<meta name="keywords" content="Aniflix, Profile, User" />
</svelte:head>

{#if isLoading}
	<LoadingScreen />
{/if}

<div class="mx-auto w-full max-w-[1096px] px-[4%]">
	<h1 class="my-4 border-l-2 border-primary px-4 text-3xl font-bold">Your Account</h1>
	<div class="mx-auto mt-8 flex max-w-[90%] flex-col space-y-4">
		<ProfileSettings />
		<EmailSettings />
		<PasswordSettings />
	</div>
</div>
