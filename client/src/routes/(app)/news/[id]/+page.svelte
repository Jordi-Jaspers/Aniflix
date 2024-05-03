<script lang="ts">
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {page} from '$app/stores';
    import {Clock, Link} from "lucide-svelte";
    import {Badge} from "$lib/components/ui/badge";
    import {getNewsTopicColor} from "$lib/api/constants";
    import {Root, Trigger, Content} from "$lib/components/ui/tooltip/index.js";
    import toast from "svelte-french-toast";

    let post: NewsPostResponse;
    onMount(async () => {
        const id: string = $page.params.id.toString();
        const response: Response = await curl(SERVER_URLS.NEWS_DETAILS_PATH.replace('{id}', id), {method: 'GET'});

        if (response.ok) {
            post = await response.json();
        }
    });

    // Function to get time since upload
    function calculateReadingTime(text: string, wordsPerMinute: number = 150): number {
        const words = text.trim().split(/\s+/).length;
        const minutes = words / wordsPerMinute;
        return Math.ceil(minutes);
    }

    // Function to copy link to clipboard
    function copyToClipboard() {
        try {
            navigator.clipboard.writeText(window.location.href);
            toast.success("Successfully copied link to clipboard.", {
                duration: 5000,
                position: 'bottom-center',
                style: 'background: #262626; color: #ffffff;'
            });
        } catch (err) {
            toast.error("Failed to copy link to clipboard.", {
                duration: 5000,
                position: 'bottom-center',
                style: 'background: #262626; color: #ffffff;'
            });
        }
    }
</script>

{#if post}

    <div class="px-[4%] max-w-[1024px] w-full mx-auto">
        <img src={post.thumbnail} alt={post.title} class="w-full h-[20rem] object-cover mb-8 rounded-[0.75rem]"/>

        <div class="space-y-4">
            <h1>{post.title}</h1>
            <div class="flex space-x-2">
                <Badge class="items-center w-fit flex space-x-1" style="background-color: {getNewsTopicColor(post.topic)}">
                    <p class="text-sm text-muted dark:text-white">{post.topic}</p>
                </Badge>
                <Badge class="bg-foreground/60 hover:bg-foreground/60  items-center w-fit flex space-x-1">
                    <Clock class="h-4 w-auto aspect-square text-muted"/>
                    <p class="text-sm text-muted">{calculateReadingTime(post.description + post.intro)} min</p>
                </Badge>
                <Badge class="bg-foreground/60 hover:bg-foreground/60  items-center w-fit">
                    <p class="text-sm text-muted">{new Date(post.uploadedAt).toLocaleTimeString()}
                        , {new Date(post.uploadedAt).toDateString()}</p>
                </Badge>

                <Root openDelay=100>
                    <Trigger>
                        <button class="items-center" on:click={copyToClipboard}>
                            <Link class="h-4 w-auto aspect-square"/>
                        </button>
                    </Trigger>
                    <Content>
                        <p>Copy link to clipboard</p>
                    </Content>
                </Root>
            </div>
        </div>

        <div class="[&:not(:first-child)]:mt-6">
            <p class="text-justify text-muted-foreground leading-7">
                {post.intro}
            </p>
            <p class=" text-justify leading-7 [&:not(:first-child)]:mt-6">{post.description}</p>
            <p class="text-justify leading-7 mt-6 italic">source: <a class="hover:underline" href="{post.url}">Anime News Network</a></p>
        </div>
    </div>
{/if}


