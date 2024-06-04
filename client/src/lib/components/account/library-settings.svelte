<script lang="ts">
    import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from '$lib/components/ui/card/index.js';
    import {Label} from '$lib/components/ui/label/index.js';
    import {Button} from '$lib/components/ui/button/index.js';
    import toast from 'svelte-french-toast';
    import {SERVER_URLS} from '$lib/api/paths';
    import {curl} from '$lib/api/client';
    import {writable} from 'svelte/store';
    import type {Writable} from 'svelte/store';
    import {Textarea} from '$lib/components/ui/textarea';

    let isLoading: Writable<boolean> = writable(false);
    let json = writable<string>('');

    async function handleSubmit() {
        $isLoading = true;
        const response: Response = await curl(SERVER_URLS.LIBRARY_IMPORT_PATH, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(json)
        });

        if (response.ok) {
            toast('Successfully received the json. Importing asynchronously, please wait.');
        }
        $isLoading = false;
    }

    async function handleExport() {
        $isLoading = true;
        toast('Exporting your library preferences. Please wait.', {
            duration: 5000,
            position: 'bottom-center',
            style: 'background: #262626; color: #ffffff;'
        });
        $isLoading = false;
    }
</script>

<form id="import-library" on:submit|preventDefault={handleSubmit}>
    <Card>
        <CardHeader class="space-y-1">
            <CardTitle class="text-2xl">Library Preferences (Currently in development)</CardTitle>
            <div class="space-y-4">
                <CardDescription>
                    Import your library preferences from a JSON file. This will overwrite your current library preferences. There is also an
                    option to
                    export your library preferences. The exported json will be saved to your clipboard.
                </CardDescription>
            </div>
        </CardHeader>
        <CardContent class="space-y-4">
            <div class="grid gap-2">
                <Label>First Name</Label>
                <Textarea id="json" disabled placeholder="Paste your json here" bind:value={$json}/>
            </div>
        </CardContent>
        <CardFooter class="my-4 space-x-2">
            <Button form="import-library" type="submit" class="w-fit" disabled>
                {#if $isLoading}
                    <div
                            class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
                            role="status"
                    />
                {:else}
                    <span>Import Library</span>
                {/if}
            </Button>
            <Button class="w-fit" disabled={$isLoading} on:click={handleExport}>
                {#if $isLoading}
                    <div
                            class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
                            role="status"
                    />
                {:else}
                    <span>Export Library</span>
                {/if}
            </Button>
        </CardFooter>
    </Card>
</form>
