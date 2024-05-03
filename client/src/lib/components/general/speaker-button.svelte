<script lang="ts">
    import {Volume2, VolumeX} from 'lucide-svelte';
    import {Button, type Events} from "$lib/components/ui/button";

    type $$Events = Events;
    export let isMuted: boolean = false;
</script>

<button class="w-auto aspect-square h-full p-1.5 rounded-full border-2 transition opacity-60 hover:opacity-100 border-card-foreground dark:border-[gray]"
    on:click
    on:keydown>
    {#if isMuted}
        <VolumeX/>
    {:else}
        <Volume2/>
    {/if}
</button>


