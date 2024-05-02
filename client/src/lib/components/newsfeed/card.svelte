<script lang="ts">
    import {Skeleton} from "$lib/components/ui/skeleton/index.js";

    export let post: NewsPostResponse;
</script>

{#if post}
    <div class="relative border-2 hover:border-primary rounded-[0.75rem] overflow-hidden h-full">
        <div class="h-full w-full bg-no-repeat bg-cover bg-center" style="background-image: url({post.thumbnail})">
            <div class="h-full w-full bg-gradient-to-b from-transparent lg:to-[99%] to-black/40 ">
                <div class="h-full w-full bg-gradient-to-t from-transparent lg:to-[99%] to-black/20"/>
            </div>
        </div>
        <p class="absolute top-2 left-2 flex items-center space-x-2 text-white shadow-2xl">
            <span class="w-3 h-3 bg-primary rounded-full"/>
            <span class="text-sm font-semibold shadow-2xl">{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</span>
        </p>

        <p class="absolute bottom-4 text-md font-bold px-2 text-white">
            {post.title}
        </p>
    </div>
{:else}
    <Skeleton/>
{/if}
