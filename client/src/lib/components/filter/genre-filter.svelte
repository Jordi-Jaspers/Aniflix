<script lang="ts">
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
    import {Button} from "$lib/components/ui/button/index.js";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {Check} from "lucide-svelte";

    export let genre: string[] = [];
    function handleCheckboxChange(event: Event, selected: string) {
        const isChecked = (event.target as HTMLInputElement).checked;
        if (isChecked) {
            genre = [...genre, selected];
        } else {
            genre = genre.filter(s => s !== selected);
        }
    }

    let possibleGenres: string[] = [];
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, {method: 'GET'});
        if (response.ok) {
            const constants: ConstantResponse = await response.json();
            possibleGenres = constants.genres;
        }
    });

    let isActive = false;
    $: {
        isActive = genre.length > 0;
    }
</script>

<DropdownMenu.Root>
    <DropdownMenu.Trigger asChild let:builder>
        <Button variant="outline" class="{isActive ? 'border-primary' : ''}" builders={[builder]}>Genres</Button>
    </DropdownMenu.Trigger>
    <DropdownMenu.Content class="w-56 max-h-48 overflow-y-auto scroll-smooth">
        {#each possibleGenres as item}
            <div class="flex items-center p-2 cursor-pointer space-x-2 hover:bg-muted rounded-md" >
                <Check class="{genre.includes(item) ? 'text-foreground' : 'text-transparent'}"/>
                <label class="flex items-center w-full relative text-sm">
                    <input class="hidden"
                           checked={genre.includes(item)}
                           type="checkbox"
                           on:change={(event) => handleCheckboxChange(event, item)}
                    />
                    <span>{item.replace(/_/g, " ").toLowerCase().replace(/\b\w/g, l => l.toUpperCase())}</span>
                </label>
            </div>
        {/each}
    </DropdownMenu.Content>
</DropdownMenu.Root>
