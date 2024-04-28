<script lang="ts">
    import {Content, Next, Previous, Root} from "$lib/components/ui/carousel";
    import {ChevronRight} from "lucide-svelte";

    export let title: string;
    export let genre: string = '';
</script>

<div class="px-[4%] lg:px-[60px] pt-8">
    <div class="py-2 flex justify-between">
        <h1 class="text-white font-bold text-lg py-1">{genre} {title}</h1>
        <button class="flex">
            See All
            <ChevronRight/>
        </button>
    </div>
    <Root class="w-full">
        <Content>
            <slot/>
        </Content>
        <Previous class="hidden md:flex absolute inset-0 top-[50%] left-[-2%] "/>
        <Next class="hidden md:flex absolute top-[50%] right-[-2%]"/>
    </Root>
</div>



