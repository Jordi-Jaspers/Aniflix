<script lang="ts">
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {page} from '$app/stores';

    let post: NewsPostResponse;
    onMount(async () => {
        const id: string = $page.params.id.toString();
        const response: Response = await curl(SERVER_URLS.NEWS_DETAILS_PATH.replace('{id}', id), {method: 'GET'});

        if (response.ok) {
            post = await response.json();
        }
    });
</script>

{#if post}
    <pre>{JSON.stringify(post, null, 2)}</pre>

    <h1>{post.title}</h1>
{/if}


