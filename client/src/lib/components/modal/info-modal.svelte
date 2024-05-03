<script lang="ts">
    import {useModalInfo, useShowInfoModal} from "$lib/components/store/store";
    import {PlayIcon, StarIcon, X} from "lucide-svelte";
    import SveltePlayer from "svelte-player";
    import type {RecursivePartial} from "svelte-player/dist/players/utility.types";
    import type {PlayerConfig} from "svelte-player/dist/players/types";
    import {Button} from "$lib/components/ui/button";
    import {LibraryButton, LikeButton, SpeakerButton} from "$lib/components/general/index.js";
    import {Badge} from "$lib/components/ui/badge";
    import {closeModal} from "$lib/api/util";
    import {EpisodeList, RecommendationCard, RecommendationCards} from "$lib/components/browse";
    import {Root, List, Trigger, Content} from "$lib/components/ui/tabs";

    let isPlaying: boolean = false;
    let isMuted: boolean = false;

    const opts: RecursivePartial<PlayerConfig> = {
        youtube: {
            playerVars: {
                'autoplay': 1,
                'controls': 0,
                'https': 1,
                'wmode': 'opaque',
                'disablekb': 1,
            },
        },
    };

    function handleEscape(e: KeyboardEvent) {
        if (e.key === 'Escape') {
            closeModal();
        }
    }
</script>

{#if $useShowInfoModal}
    <button on:click={() => closeModal()} class="!z-[100] fixed backdrop-brightness-50 w-full h-full inset-0"/>
    <div role="presentation" on:close={() => closeModal()} on:keypress={handleEscape}
         class="!z-[1000] fixed bg-background inset-0 mx-auto h-auto w-full md:rounded-t-md md:top-8 md:max-w-[90%] lg:max-w-4xl overscroll-auto overflow-y-scroll">
        <button class="absolute m-[1em] top-0 right-0 h-9 w-9 flex justify-center items-center bg-[#1a1920]/80 hover:bg-[#1a1920] rounded-full text-white !z-40 onclick"
                on:click={() => closeModal()}>
            <X class="h-6 w-6"/>
        </button>
        <div class="relative aspect-video w-full !z-[-100]">
            <div class="absolute right-0 aspect-video h-full bg-gradient-to-b from-transparent to-background"/>
            <img class="w-full aspect-video object-cover object-center brightness-85 {isPlaying && 'hidden'}"
                 src={$useModalInfo.anime.cover}
                 alt="thumbnail"/>
            <div class="h-full brightness-85  {isPlaying ? '' : 'hidden'}">
                <SveltePlayer url="https://www.youtube.com/watch?v={$useModalInfo.anime.trailer}"
                              config={opts}
                              height="100%"
                              width="100%"
                              bind:muted={isMuted}
                              on:play={() => isPlaying = true}
                              on:pause={() => isPlaying = false}
                              on:ended={() => isPlaying = false}>
                    <div slot="play-icon"/>
                </SveltePlayer>
            </div>
            <div class="absolute bottom-10 flex w-full items-center justify-between px-10">
                <div class="flex items-center space-x-2">
                    <Button class="pl-4 pr-1 space-x-4 rounded-full opacity-80 transition hover:opacity-100">
                        <p>Watch Now</p>
                        <div class="rounded-full bg-secondary/50 w-12 p-1 flex justify-center">
                            <PlayIcon class="p-1 fill-white"/>
                        </div>
                    </Button>

                    <div class="flex justify-center h-10 space-x-2">
                        <LikeButton value={$useModalInfo.anime}/>
                        <LibraryButton value={$useModalInfo.anime}/>
                    </div>
                </div>
                {#if isPlaying}
                        <SpeakerButton isMuted={isMuted} on:click={() => isMuted = !isMuted}/>
                {/if}
            </div>
        </div>
        <div class="px-10 items-center space-y-6 text-lg w-full flex-col justify-center z-10">
            <div class="space-y-2">
                <h3 class="flex items-center">
                    {$useModalInfo.anime.title.split(' ')
                        .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')
                        .split('-')
                        .map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join('-')
                    }
                    <Badge variant="outline" class="bg-blue-500 ml-4">
                        {#if $useModalInfo.anime.subbed} SUB{:else} DUB{/if}
                    </Badge>
                </h3>

                <div class="flex items-center text-[1.1vw] h-[1.2vw] w-[1.2] text-muted-foreground text-sm">
                    <div class="flex items-center space-x-1">
                        <p> {$useModalInfo.anime.rating / 10} </p>
                        <StarIcon class="ml-0.5 h-3 w-fit text-amber-300 fill-amber-300"/>
                    </div>

                    <span class="mx-2"> | </span>
                    <span> {$useModalInfo.anime.releaseYear} </span>
                    <span class="mx-2"> | </span>
                    <span> {$useModalInfo.anime.totalEpisodes} Episodes </span>
                    <p class="mx-2 flex h-4 items-center justify-center rounded border px-1.5 text-xs">
                        HD
                    </p>
                </div>
            </div>


            <div class="flex flex-col gap-x-10 gap-y-4 font-light w-full md:flex-row">
                <article class="w-[85%] font-extralight leading-7 text-justify text-sm prose">
                    {@html $useModalInfo.anime.description.replace(/\(Source:.*\)/, '')}
                </article>
                <div class="flex flex-col text-sm space-y-4">
                    <p>
                        <span class="text-muted-foreground">Genres:</span>{' '}
                        {$useModalInfo.anime.genres
                            .filter(genre => genre !== "UNKNOWN")
                            .map(genre => genre.charAt(0) + genre.slice(1).toLowerCase())
                            .join(', ')
                        }
                    </p>
                    <p>
                        <span class="text-muted-foreground">Media:</span>{' '}
                        {$useModalInfo.anime.mediaType}
                    </p>
                    <p>
                        <span class="text-muted-foreground">Status:</span>{' '}
                        {$useModalInfo.anime.status.charAt(0) + $useModalInfo.anime.status.slice(1).toLowerCase()}
                    </p>
                </div>
            </div>
            <Root value="Episodes" class="py-8">
                <List class="bg-transparent space-x-4">
                    <Trigger value="Episodes" class="p-0 text-xl data-[state=active]:bg-transparent data-[state=active]:border-b-2 border-primary rounded-none" >Episodes</Trigger>
                    <Trigger value="Recommended" class="p-0 text-xl data-[state=active]:bg-transparent data-[state=active]:border-b-2 border-primary rounded-none">Recommended</Trigger>
                </List>
                <Content value="Episodes" >
                    <EpisodeList episodes={$useModalInfo.anime.episodes}/>
                </Content>
                <Content value="Recommended">
                    <RecommendationCards anime={$useModalInfo.anime}/>
                </Content>
            </Root>
        </div>
    </div>
{/if}
