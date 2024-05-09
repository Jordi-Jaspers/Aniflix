<script lang="ts">
    import {Content, Item, Root} from "$lib/components/ui/select";
    import {Gauge} from "lucide-svelte";
    import type {Selected} from "bits-ui";
    import {Select as SelectPrimitive} from "bits-ui";

    export let playbackRate;

    let currentPlaybackRate: Selected<number> = {value: playbackRate, label: {playbackRate} + "x"};
    function setPlaybackRate(selectedPlaybackRate: Selected<number> | undefined) {
        if (selectedPlaybackRate) {
            currentPlaybackRate = selectedPlaybackRate;
            playbackRate = currentPlaybackRate.value;
        }
    }
</script>

<Root onSelectedChange={(selectedPlaybackRate) => setPlaybackRate(selectedPlaybackRate)} selected={currentPlaybackRate}>
    <SelectPrimitive.Trigger
            class="flex h-full items-center justify-between disabled:cursor-not-allowed disabled:opacity-50">
        <button aria-label="Playback" class="h-full w-full">
            <Gauge class="aspect-square h-full w-auto hover:scale-125"/>
        </button>
    </SelectPrimitive.Trigger>
    <Content class="z-[1000] !w-fit max-h-[25%] overflow-y-auto scroll-smooth">
        <Item value={0.5} label="0.5x"/>
        <Item value={0.75} label="0.75x"/>
        <Item value={1} label="1x"/>
        <Item value={1.5} label="1.5x"/>
        <Item value={2} label="2x"/>
    </Content>
</Root>
