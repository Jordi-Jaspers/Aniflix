<script lang="ts">
    import {Description, Root, Title} from '$lib/components/ui/alert';
    import {ShieldBan} from 'lucide-svelte';
    import {useErrorMessage, useHasError} from "$lib/components/store/store";
    import {onDestroy, onMount} from "svelte";

    let fadeOutTimer: NodeJS.Timeout;
    function startFadeOutTimer() {
        // Clear any existing timers
        clearTimeout(fadeOutTimer);
        // Set a new timer to fade out after 5 seconds
        fadeOutTimer = setTimeout(() => {
            $useHasError = false; // Hide the component
        }, 5000);
    }

    // Call the startFadeOutTimer function on component mount
    onMount(startFadeOutTimer);

    // Clear the timer when the component is destroyed
    onDestroy(() => {
        clearTimeout(fadeOutTimer);
    });
</script>

<Root variant="destructive"
      class="{$useHasError ? 'flex opacity-100 transition-opacity duration-500' : 'opacity-0'} fixed bottom-5 left-0 right-0 mx-auto w-[95%] flex space-x-2 bg-red-200/70 py-4">
    <ShieldBan class="h-[50%] w-6"/>
    <div>
        <Title>Error</Title>
        <Description>{$useErrorMessage}</Description>
    </div>
</Root>
