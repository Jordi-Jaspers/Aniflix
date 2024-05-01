<script lang="ts">
    import {goto} from '$app/navigation';
    import Header from '$lib/components/app/header/header.svelte';
    import Footer from '$lib/components/app/footer/footer.svelte';
    import {useHasAuthError, useShowInfoModal} from '$lib/components/store/store';
    import {isUserAuthenticated} from "$lib/api/client";
    import {CLIENT_URLS} from "$lib/api/paths";
    import {InfoModal} from "$lib/components/modal/index.js";
    import {onMount} from 'svelte';

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
    <main class="h-screen {$useShowInfoModal && 'noscroll overflow-hidden'}">
        <InfoModal/>
        <div class="relative flex flex-col">
            <Header/>
            <slot/>
        </div>
        <Footer/>
    </main>
{/if}
