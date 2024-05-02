<script lang="ts">
    import {useAuthenticated, useHasError} from '$lib/components/store/store';
    import {onMount} from 'svelte';
    import '../app.pcss';
    import toast, {Toaster} from "svelte-french-toast";
    import {ModeWatcher} from "mode-watcher";

    let hasError: boolean;
    onMount(() => {
        useHasError.subscribe((value) => {
            hasError = value;
            if (hasError && !useAuthenticated) {
                toast.error("You are unauthorized or your session has expired.", {
                    duration: 5000,
                    position: 'bottom-center',
                    style: 'background: #262626; color: #ffffff;'
                });
            }
        });
    });
</script>

<main>
    <ModeWatcher />
    <slot class="bg-[#1a1920]"/>
    <Toaster />
</main>
