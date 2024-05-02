<script lang="ts">
    import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
    import {
        Bug,
        ChevronRight,
        CircleUserRound,
        Coffee,
        Compass,
        Library,
        LogOut,
        Menu, Newspaper,
        User
    } from 'lucide-svelte';
    import {curl, logout} from "$lib/api/client";
    import {onMount} from "svelte";
    import {SERVER_URLS} from "$lib/api/paths";
    import {Button} from "$lib/components/ui/button";

    let user: UserDetailsResponse;
    onMount(async () => {
        const response: Response = await curl(SERVER_URLS.USER_DETAILS_PATH, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })

        if (response.ok) {
            user = await response.json();
        }
    })

    let isOpen: boolean = false;

    function handleClick() {
        isOpen = !isOpen;
    }

    function greetingsMessage() {
        const username = user.firstName;
        const date = new Date();
        const hours = date.getHours();
        if (hours < 12) {
            return 'Good morning, ' + username;
        } else if (hours < 18) {
            return 'Good afternoon, ' + username;
        } else {
            return 'Good evening, ' + username;
        }
    }
</script>

{#if user}
    <DropdownMenu.Root onOpenChange={handleClick}>
        <DropdownMenu.Trigger class="h-full max-h-[24px] space-x-2 flex items-center">
            <Button variant="ghost" size="icon" class="flex md:hidden">
                <Menu />
            </Button>
                <CircleUserRound class="hidden md:flex"/>
                <div class="flex-col text-start hidden md:flex">
                    <span class="text-xs font-bold">{user.firstName} {user.lastName}</span>
                    <span class="font-extralight text-[0.5em]">{user.authorities}</span>
                </div>

            <ChevronRight class="h-4 w-4 duration-200 hidden md:flex {isOpen && 'rotate-90'}"/>
        </DropdownMenu.Trigger>
        <DropdownMenu.Content class="mt-4 w-fit min-w-[20%]">
            <DropdownMenu.Label class="text-center mx-8">{greetingsMessage()}</DropdownMenu.Label>
            <DropdownMenu.Separator class="block md:hidden"/>
            <DropdownMenu.Group class="block md:hidden">
                <DropdownMenu.Item>
                    <Newspaper class="mr-2 h-4 w-4"/>
                    <span>News</span>
                </DropdownMenu.Item>
                <DropdownMenu.Item>
                    <Compass class="mr-2 h-4 w-4"/>
                    <span>Browse</span>
                </DropdownMenu.Item>
                <DropdownMenu.Item>
                    <Library class="mr-2 h-4 w-4"/>
                    <span>Library</span>
                </DropdownMenu.Item>
            </DropdownMenu.Group>
            <DropdownMenu.Separator/>
            <DropdownMenu.Group>
                <DropdownMenu.Item>
                    <User class="mr-2 h-4 w-4"/>
                    <span>Profile</span>
                </DropdownMenu.Item>
                <DropdownMenu.Item>
                    <Coffee class="mr-2 h-4 w-4"/>
                    <span>Donate Coffee</span>
                </DropdownMenu.Item>
            </DropdownMenu.Group>
            <DropdownMenu.Separator/>
            <DropdownMenu.Item class="data-[highlighted]:bg-primary/75">
                <Bug class="mr-2 h-4 w-4"/>
                <span>Report Bug</span>
            </DropdownMenu.Item>
            <DropdownMenu.Item on:click={() => logout()} class="data-[highlighted]:bg-primary/75">
                <LogOut class="mr-2 h-4 w-4"/>
                <span>Log out</span>
            </DropdownMenu.Item>
        </DropdownMenu.Content>
    </DropdownMenu.Root>
{/if}
