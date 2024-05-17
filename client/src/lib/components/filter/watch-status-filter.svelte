<script lang="ts">
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu/index.js";
    import {Button} from "$lib/components/ui/button/index.js";
    import {onMount} from "svelte";
    import {curl} from "$lib/api/client";
    import {SERVER_URLS} from "$lib/api/paths";
    import {Check} from "lucide-svelte";

    export let watchStatus: string[] = [];
    function handleCheckboxChange(event: Event, status: string) {
        const isChecked = (event.target as HTMLInputElement).checked;
        if (isChecked) {
            watchStatus = [...watchStatus, status];
        } else {
            watchStatus = watchStatus.filter(s => s !== status);
        }
    }

    let possibleStatus: string[] = [];
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.ANIME_CONSTANT_PATH, {method: 'GET'});
        if (response.ok) {
            const constants: ConstantResponse = await response.json();
            possibleStatus = constants.watchStatus;
        }
    });

    let isActive = false;
    $: {
        isActive = watchStatus.length > 0;
    }
</script>

<DropdownMenu.Root>
    <DropdownMenu.Trigger asChild let:builder>
        <Button variant="outline" class="{isActive ? 'border-primary' : ''}" builders={[builder]}>Watch Status</Button>
    </DropdownMenu.Trigger>
    <DropdownMenu.Content class="w-56 max-h-48 overflow-y-auto scroll-smooth">
        {#each possibleStatus as status}
            <div class="flex items-center p-2 cursor-pointer space-x-2 hover:bg-muted rounded-md" >
                <Check class="{watchStatus.includes(status) ? 'text-foreground' : 'text-transparent'}"/>
                <label class="flex items-center w-full relative text-sm">
                    <input class="hidden"
                           checked={watchStatus.includes(status)}
                           type="checkbox"
                           on:change={(event) => handleCheckboxChange(event, status)}
                    />
                    <span>{status.replace(/_/g, " ").toLowerCase().replace(/\b\w/g, l => l.toUpperCase())}</span>
                </label>
            </div>
        {/each}
    </DropdownMenu.Content>
</DropdownMenu.Root>
