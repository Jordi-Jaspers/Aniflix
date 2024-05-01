<script lang="ts">
	import {page} from '$app/stores';
	import Logo from '$lib/assets/icons/aniflix-logo-large.png';
</script>

{#if $page.status === 404}
	<div class="flex flex-col items-center justify-center h-screen space-y-4">
		<div class="mx-auto flex w-[350px] flex-col justify-center space-y-6">
			<div class="px-16">
				<img src={Logo} alt="AniFlix Logo" width={941} height={313}/>
			</div>
		</div>
		<h1 class="text-4xl font-bold mb-4">404 - Page Not Found</h1>
		<p class="text-lg mb-8">Oops! The page you're looking for doesn't exist.</p>
		<a href="/browse" class="text-blue-500 hover:underline">Go back to the homepage</a>
	</div>
{/if}
