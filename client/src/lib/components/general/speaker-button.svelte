<script lang="ts">
    import {Volume2, VolumeX} from 'lucide-svelte';
    import {Button} from "$lib/components/ui/button";

    export let isMuted: boolean = false;
</script>

<Button class="h-[40px] w-[40px] flex p-2 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:bg-[#2a2a2a]/60 hover:border-white">
    {#if isMuted}
        <VolumeX class="h-[24px] w-[24px]"/>
    {:else}
        <Volume2 class="h-[24px] w-[24px]"/>
    {/if}
</Button>
