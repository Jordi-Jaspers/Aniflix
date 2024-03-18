<script lang="ts">
    import {fade} from 'svelte/transition';
    import {Description, Root, Title} from '$lib/components/ui/alert';
    import {ShieldBan} from 'lucide-svelte';

    export let isVisible: boolean = false;
    export let error: string = '';
</script>

{#if isVisible}
    <div transition:fade>
        <Root class="absolute bottom-10 left-0 right-0 mx-auto w-[95%] flex space-x-2 bg-red-200/70 py-4" variant="destructive">
            <ShieldBan class="h-[50%] w-6"/>
            <div>
                <Title>Error</Title>
                <Description>{error}</Description>
            </div>
        </Root>
    </div>
{/if}
