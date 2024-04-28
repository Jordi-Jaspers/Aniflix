<script lang="ts">
    import {goto} from '$app/navigation';
    import Header from '$lib/components/app/header/header.svelte';
    import Footer from '$lib/components/app/footer/footer.svelte';
    import {useHasAuthError, useShowInfoModal} from '$lib/components/store/store';
    import ErrorMessage from '$lib/components/general/error-message.svelte';
    import {onMount} from 'svelte';
    import {isUserAuthenticated} from "$lib/api/client";
    import {CLIENT_URLS} from "$lib/api/paths";
    import {InfoModal} from "$lib/components/modal/index.js";

    let isAuthenticated: boolean;
    onMount(async () => {
        isAuthenticated = await isUserAuthenticated();
        if (!isAuthenticated) {
            goto(CLIENT_URLS.LOGIN_URL);
            $useHasAuthError = true;
        }
    });
</script>

{#if isAuthenticated}
    <main class="h-screen {$useShowInfoModal && 'overflow-y-hidden noscroll'}">
        <InfoModal/>
        <div class="relative flex flex-col">
            <Header/>
            <slot/>
        </div>
        <ErrorMessage/>
        <Footer/>
    </main>
{/if}
