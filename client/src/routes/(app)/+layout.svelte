<script lang="ts">
    import {goto} from '$app/navigation';
    import Header from '$lib/components/app/header/header.svelte';
    import Footer from '$lib/components/app/footer/footer.svelte';
    import {useErrorMessage, useHasError, useShowInfoModal} from '$lib/store';
    import ErrorMessage from '$lib/components/general/error-message.svelte';

    import {onMount} from 'svelte';
    import {isUserAuthenticated} from "$lib/api/client";
    import {CLIENT_URLS} from "$lib/api/paths";
    import {InfoModal} from "$lib/components/modal/index.js";

    let isAuthenticated: boolean;
    let errorMessage: string | undefined;
    let hasError: boolean = false;

    onMount(async () => {
        isAuthenticated = await isUserAuthenticated();
        if (!isAuthenticated) {
            goto(CLIENT_URLS.LOGIN_URL);
            useHasError.set(true);
        }

        useHasError.subscribe((value) => hasError = value);
        useErrorMessage.subscribe((value) => errorMessage = value);
    });
</script>

{#if isAuthenticated}
    <main class="h-screen {$useShowInfoModal && 'overflow-y-hidden noscroll'}">
        <InfoModal/>
        <div class="relative flex flex-col">
            <Header/>
            <slot/>
        </div>
        <ErrorMessage isVisible={hasError} error={errorMessage}/>
        <Footer/>
    </main>
{/if}
