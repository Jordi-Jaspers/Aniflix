<script lang="ts">
   import {onMount} from "svelte";
   import {curl} from "$lib/api/client";
   import {ChevronLeft, ChevronRight} from "lucide-svelte";

   export let title: string;
   export let genre: string = '';
   export let url: string;

   let collection: AnimeResponse[];
   onMount(async () => {
       const body: AnimeRequest = {
           page: 1,
           perPage: 25,
           title: '',
           genre: genre,
           season: ''
       };

       const response: Response = await curl(url, {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(body)
       });

       if (response.ok) {
           collection = await response.json();
       }
   });
</script>

{#if collection && collection.length > 0}
    <div class="pt-8">
        <div class="py-2 flex justify-between">
            <h1 class="text-white font-bold text-lg py-1">{genre} {title}</h1>
            <button class="flex">
                See All
                <ChevronRight/>
            </button>
        </div>
        <div class="group relative md:-ml-2">
            <div class={"arrowIcon left-0 bg-gradient-to-r items-center"}>
                <ChevronLeft class="opacity-0 transition hover:scale-125 group-hover:opacity-100 h-[60%] w-[60%]"/>
            </div>
            <div class="w-screen flex items-center space-x-1 overflow-x-scroll overflow-hidden scrollbar-hide md:space-x-3 px-4 md:px-6 lg:px-12">
            cards
            </div>
            <div class={"arrowIcon right-0 bg-gradient-to-l items-center"}>
                <ChevronRight class="opacity-0 transition hover:scale-125 group-hover:opacity-100 h-[60%] w-[60%] lg:h-[100%] lg:w-[100%] "/>
            </div>
        </div>
    </div>
{/if}
