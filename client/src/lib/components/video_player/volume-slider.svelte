<script lang="ts">
	import { Volume, Volume1, Volume2, VolumeX } from 'lucide-svelte';
	import { Slider } from 'bits-ui';
	import Device from 'svelte-device-info';

	export let volume: number;
	export let muted: boolean;
	export let showVolume: boolean;

	let prevVolume: number = 1;

	function toggleMute() {
		if (!muted) {
			prevVolume = volume;
			muted = true;
			volume = 0;
		} else {
			volume = prevVolume;
			muted = false;
		}
	}
</script>

<div
	class="group relative !z-20 flex space-x-2"
	on:mouseenter={() => (showVolume = true)}
	on:mouseleave={() => (showVolume = false)}
	role="group"
>
	<button aria-label="mute" class="h-full" on:click={toggleMute}>
		{#if muted || volume === 0}
			<VolumeX class="aspect-square h-full w-auto hover:scale-125" />
		{:else if volume > 0 && volume < 0.25}
			<Volume class="aspect-square h-full w-auto hover:scale-125" />
		{:else if volume > 0.25 && volume < 0.75}
			<Volume1 class="aspect-square h-full w-auto hover:scale-125" />
		{:else}
			<Volume2 class="aspect-square h-full w-auto hover:scale-125" />
		{/if}
	</button>

	{#if !Device.isMobile && !Device.isTablet}
		<Slider.Root
			value={[volume]}
			min={0}
			max={1}
			step={0.01}
			orientation="vertical"
			onValueChange={(value) => (volume = value[0])}
			let:thumbs
			class="absolute bottom-[115%] right-[50%] hidden h-[10vw] max-h-32 min-h-16 touch-none select-none items-center group-hover:flex"
		>
			<span class="relative h-full w-2 grow overflow-hidden rounded-full bg-muted">
				<Slider.Range class="absolute w-full bg-primary" />
			</span>
			{#each thumbs as thumb}
				<Slider.Thumb
					{thumb}
					class="border-border-input hover:border-dark-40 active:scale-98 block size-[16px]
                                -translate-x-1 cursor-pointer rounded-full border bg-background shadow
                                ring-1 ring-primary transition-colors hover:scale-125
                                focus-visible:outline-none disabled:pointer-events-none disabled:opacity-50 dark:bg-background dark:shadow-card"
				/>
			{/each}
		</Slider.Root>
	{/if}
</div>
