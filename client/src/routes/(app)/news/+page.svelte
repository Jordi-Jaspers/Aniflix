<script lang="ts">
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";

    let newsFeed: NewsPostResponse[];
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.NEWS_PATH, {method: 'GET'});
        if (response.ok) {
            newsFeed = await response.json();
        }
    });
</script>

<h1>News Feed</h1>
<pre>
    {JSON.stringify(newsFeed, null, 2)}
</pre>
