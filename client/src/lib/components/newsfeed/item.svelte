<script lang="ts">
    import {Skeleton} from "$lib/components/ui/skeleton/index.js";
    import {getNewsTopicColor} from "$lib/api/constants";
    import {goto} from "$app/navigation";
    import {CLIENT_URLS} from "$lib/api/paths";

    export let post: NewsPostResponse;

    // Function to get time since upload
    function getTimeSinceUpload(date: Date) {
        const now = new Date();
        const diff = now.getTime() - date.getTime();
        const diffInMinutes = Math.floor(diff / 60000);
        if (diffInMinutes < 60) {
            return `${diffInMinutes} minutes ago`;
        }
        const diffInHours = Math.floor(diffInMinutes / 60);
        if (diffInHours < 24) {
            return `${diffInHours} hours ago`;
        }
        const diffInDays = Math.floor(diffInHours / 24);
        return `${diffInDays} days ago`;
    }
</script>

{#if post}
    <div class="relative w-full rounded-[0.75rem] flex flex-col text-center space-y-4 hover:bg-muted md:h-[36rem] p-4 md:p-2">
        <button class="absolute h-full w-full" on:click={() => goto(CLIENT_URLS.NEWS_URL + "/" + post.id.toString())}/>
        <img class="aspect-square w-full rounded-[0.75rem] object-cover" src="{post.thumbnail}" alt="{post.title}"/>
        <div class="space-y-2 overflow-hidden">
            <div class="flex text-center items-center space-x-2 text-muted-foreground font-extralight text-xs">
                <span class="w-3 h-3 rounded-full" style="background-color: {getNewsTopicColor(post.topic)}"/>
                <p>{post.topic.charAt(0).toUpperCase() + post.topic.slice(1)}</p>
                <span class="w-1 h-1 rounded-full bg-muted-foreground"/>
                <p>{getTimeSinceUpload(new Date(post.uploadedAt))}</p>
            </div>
            <div class="text-left h-full w-full ">
                <h4>{post.title}</h4>
                <p class="text-sm text-muted-foreground leading-2 mt-2">{post.intro}</p>
            </div>
        </div>
    </div>
{:else}
    <Skeleton/>
{/if}
