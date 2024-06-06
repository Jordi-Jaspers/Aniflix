<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import {useShowChangelog} from "$lib/components/store/localstorage";
    import {Button} from "$lib/components/ui/button";
    import {MessageCircleCode} from "lucide-svelte";

    export let applicationVersion = '';
</script>

<Dialog.Root bind:open={$useShowChangelog}>
    <Dialog.Trigger>
        <span class="hover:underline">{applicationVersion}</span>
    </Dialog.Trigger>
    <Dialog.Content>
        <Dialog.Header>
            <Dialog.Title class="flex flex-col space-y-2 items-center">
                <MessageCircleCode class="w-10 h-10"/>
                <span class="font-semibold">What's new?</span>
                <span class="mt-1 font-light text-[0.75rem] text-muted-foreground italic"> Version: {applicationVersion} </span>
            </Dialog.Title>

            <Dialog.Description class="space-y-4 p-4">
                <div class="space-y-2">
                    <h4 class="text-primary-foreground text-lg">New</h4>
                    <ul class="ml-8 list-disc space-y-1">
                        <li>Introduced a loading indicator when interacting with an anime for more user feedback.</li>
                        <li>More user-friendly and interactive notifications.</li>
                        <li>Added a 'buy me a coffee' link in the menu bar</li>
                    </ul>
                </div>

                <div class="space-y-2">
                    <h4 class="text-primary-foreground text-lg">Fixed</h4>
                    <ul class="ml-8 list-disc space-y-1">
                        <li>Caching issue in the information modal.</li>
                        <li>The 'Change password' & 'Password reset' button is now disabled when the input is empty.</li>
                        <li>Added optimistic locking to prevent database issues.</li>
                    </ul>
                </div>
            </Dialog.Description>
        </Dialog.Header>

        <Dialog.Footer>
            <Button on:click={() => $useShowChangelog = false}>
                Close
            </Button>
        </Dialog.Footer>
    </Dialog.Content>
</Dialog.Root>
