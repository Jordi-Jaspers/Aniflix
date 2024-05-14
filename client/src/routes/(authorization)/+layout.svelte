<script lang="ts">
	import { isUserAuthenticated } from '$lib/api/client';
	import { CLIENT_URLS } from '$lib/api/paths';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	onMount(async () => {
		if (await isUserAuthenticated()) {
			goto(CLIENT_URLS.BROWSE_URL);
		}
	});
</script>

<head>
	<title>Login - Aniflix</title>
	<meta name="description" content="Login or Register to AniFlix.stream" />
	<meta name="keywords" content="AniFlix, Login, Register, Authentication" />
</head>

<slot class="flex h-screen flex-col" />
