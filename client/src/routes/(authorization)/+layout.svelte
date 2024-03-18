<script lang="ts">
    import {isUserAuthenticated} from "$lib/api/client";
    import {CLIENT_URLS} from "$lib/api/paths";
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";

    onMount(async () => {
        if (await isUserAuthenticated()) {
            goto(CLIENT_URLS.BROWSE_URL);
        }
    });
</script>

<slot class="flex h-screen flex-col"/>
