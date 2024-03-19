<script lang="ts">
    import {BookOpen, BookOpenCheck, Library, Volume2, VolumeX} from 'lucide-svelte';
    import {Button} from "$lib/components/ui/button";

    let isAdded: boolean = false;
</script>

<Button class="h-[40px] w-[40px] flex p-2 items-center justify-center rounded-full border-2 border-[gray] bg-[#2a2a2a]/60 transition hover:bg-[#2a2a2a]/60 hover:border-white">
    {#if isAdded}
        <BookOpen class="h-[24px] w-[24px]"/>
    {:else}
        <BookOpenCheck class="h-[24px] w-[24px]"/>
    {/if}
</Button>
