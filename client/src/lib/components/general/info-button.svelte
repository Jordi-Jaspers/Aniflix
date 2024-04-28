<script lang="ts">
    import {InfoIcon} from 'lucide-svelte';
    import {openModal} from "$lib/api/util";
    import {getAnilistId} from "$lib/components/store/anime-context-store";
    import type {Writable} from "svelte/store";

    let anilistId: Writable<number> = getAnilistId();
</script>

<button on:click={() => openModal($anilistId)}>
    <InfoIcon class="w-auto aspect-square h-full rounded-full bg-[#2a2a2a]/60 transition hover:text-primary/60 hover:border-primary/60"/>
</button>
