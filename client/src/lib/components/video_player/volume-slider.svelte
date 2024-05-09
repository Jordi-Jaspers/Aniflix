<script lang="ts">
    import {Volume, Volume1, Volume2, VolumeX} from "lucide-svelte";
    import {Slider} from "bits-ui";

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


<div class="relative flex space-x-2 group !z-20" on:mouseenter={() => showVolume = true} on:mouseleave={() => showVolume = false}
     role="group">
    <button aria-label="mute" class="h-full" on:click={toggleMute}>
        {#if muted || volume === 0}
            <VolumeX class="aspect-square h-full w-auto hover:scale-125"/>
        {:else if volume > 0 && volume < 0.25}
            <Volume class="aspect-square h-full w-auto hover:scale-125"/>
        {:else if volume > 0.25 && volume < 0.75}
            <Volume1 class="aspect-square h-full w-auto hover:scale-125"/>
        {:else}
            <Volume2 class="aspect-square h-full w-auto hover:scale-125"/>
        {/if}
    </button>

    <Slider.Root value={[volume]} min={0} max={1} step={0.01} orientation="vertical"
                 onValueChange={(value) => volume = value[0]}
                 let:thumbs
                 class="hidden group-hover:flex h-[10vw] max-h-32 min-h-16 absolute bottom-[115%] right-[50%] touch-none select-none items-center">
    <span class="relative w-2 h-full grow overflow-hidden rounded-full bg-muted">
      <Slider.Range class="absolute w-full bg-primary"/>
    </span>
        {#each thumbs as thumb}
            <Slider.Thumb {thumb}
                          class="-translate-x-1 block size-[16px] cursor-pointer rounded-full
                                border border-border-input bg-background shadow transition-colors hover:border-dark-40
                                focus-visible:outline-none hover:scale-125 ring-1 ring-primary
                                active:scale-98 disabled:pointer-events-none disabled:opacity-50 dark:bg-background dark:shadow-card"/>
        {/each}
    </Slider.Root>
</div>
