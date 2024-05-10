<script>
	import Logo from '$lib/assets/icons/aniflix-logo-large.png';
</script>

<div class="fixed inset-0 !z-[1000] flex h-screen w-screen items-center justify-center bg-black">
	<div class="flex h-screen flex-col items-center justify-center space-y-4">
		<div class="mx-auto flex w-[350px] flex-col justify-center space-y-6">
			<div class="px-16">
				<img src={Logo} alt="AniFlix Logo" width={941} height={313} />
			</div>
		</div>
		<p class="text-center text-lg font-bold">Loading...</p>
		<div
			class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-primary motion-reduce:animate-[spin_1.5s_linear_infinite]"
			role="status"
		/>
	</div>
</div>
