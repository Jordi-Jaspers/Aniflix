<script lang="ts">
    import {getNewsTopicColor} from "$lib/api/constants";
    import {goto} from "$app/navigation";
    import {CLIENT_URLS} from "$lib/api/paths";

    export let post: NewsPostResponse;
</script>

{#if post}
    <div class="relative border-2 hover:border-primary rounded-[0.75rem] overflow-hidden h-full">
        <button class="absolute h-full w-full" on:click={() => goto(CLIENT_URLS.NEWS_URL + "/" + post.id.toString())}/>
        <div class="h-full w-full bg-no-repeat bg-cover bg-center" style="background-image: url({post.thumbnail})">
            <div class="h-full w-full bg-gradient-to-b from-transparent lg:to-[99%] to-black/40 ">
                <div class="h-full w-full bg-gradient-to-t from-transparent lg:to-[99%] to-black/20"/>
            </div>
        </div>
        <p class="absolute top-2 left-2 flex items-center space-x-2 text-white shadow-2xl">
            <span class="w-3 h-3 rounded-full" style="background-color: {getNewsTopicColor(post.topic)}"/>
            <span class="text-sm font-semibold shadow-2xl">{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</span>
        </p>

        <h4 class="absolute bottom-4 px-4 text-md text-white">
            {post.title}
        </h4>
    </div>
{/if}
