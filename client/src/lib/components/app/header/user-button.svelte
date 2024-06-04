<script lang="ts">
import { Bug, ChevronRight, CircleUserRound, Coffee, Compass, Library, LogOut, Menu, Newspaper, User } from 'lucide-svelte';
import { curl, logout } from '$lib/api/client';
import { onMount } from 'svelte';
import { CLIENT_URLS, SERVER_URLS } from '$lib/api/paths';
import { Button } from '$lib/components/ui/button';
import { goto } from '$app/navigation';
import { useUserDetails } from '$lib/components/store/store';
import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
import { toggleMode } from 'mode-watcher';
import Sun from 'lucide-svelte/icons/sun';
import Moon from 'lucide-svelte/icons/moon';

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

let user: UserDetailsResponse = {} as UserDetailsResponse;
async function getUserDetails() {
	const response: Response = await curl(SERVER_URLS.USER_DETAILS_PATH, {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json'
		}
	});

	if (response.ok) {
		user = await response.json();
		$useUserDetails = user;
	}
}

onMount(async () => {
	await getUserDetails();
});

$: $useUserDetails = user;
</script>

{#if user}
	<DropdownMenu.Root onOpenChange={handleClick}>
		<DropdownMenu.Trigger class="flex h-full max-h-[24px] items-center space-x-2">
			<Button variant="ghost" size="icon" class="flex md:hidden">
				<Menu />
			</Button>
			<CircleUserRound class="hidden md:flex" />
			<div class="hidden flex-col text-start md:flex">
				<span class="text-xs font-bold">{user.firstName} {user.lastName}</span>
				<span class="text-[0.5em] font-extralight">{user.authorities}</span>
			</div>
			<ChevronRight class="hidden h-4 w-4 duration-200 md:flex {isOpen && 'rotate-90'}" />
		</DropdownMenu.Trigger>
		<DropdownMenu.Content class="mt-4 w-fit min-w-[20%]">
			<DropdownMenu.Label class="mx-8 text-center">{greetingsMessage()}</DropdownMenu.Label>
			<DropdownMenu.Separator class="block md:hidden" />
			<DropdownMenu.Group class="block md:hidden">
				<DropdownMenu.Item data-sveltekit-preload-data="hover" on:click={() => {
						goto(CLIENT_URLS.BROWSE_URL);
					}}>
					<Compass class="mr-2 h-4 w-4" />
					<span>Browse</span>
				</DropdownMenu.Item>
				<DropdownMenu.Item data-sveltekit-preload-data="hover" on:click={() => {
						goto(CLIENT_URLS.LIBRARY_URL);
					}}>
					<Library class="mr-2 h-4 w-4" />
					<span>Library</span>
				</DropdownMenu.Item>
				<DropdownMenu.Item data-sveltekit-preload-data="hover" on:click={() => {
						goto(CLIENT_URLS.NEWS_URL);
					}}>
					<Newspaper class="mr-2 h-4 w-4" />
					<span>News</span>
				</DropdownMenu.Item>
			</DropdownMenu.Group>
			<DropdownMenu.Separator />
			<DropdownMenu.Group>
				<DropdownMenu.Item data-sveltekit-preload-data="hover" on:click={() => goto(CLIENT_URLS.ACCOUNT_URL)}>
					<User class="mr-2 h-4 w-4" />
					<span>Profile</span>
				</DropdownMenu.Item>
			</DropdownMenu.Group>
			<DropdownMenu.Separator />
			<DropdownMenu.Group>
				<DropdownMenu.Item on:click={toggleMode}>
					<Sun class="mr-2 h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
					<Moon class="absolute mr-2 h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
					<span>Toggle Mode</span>
				</DropdownMenu.Item>
				<DropdownMenu.Item href={import.meta.env.VITE_DONATION_URL}>
					<Coffee class="mr-2 h-4 w-4" />
					<span>Buy me a Coffee</span>
				</DropdownMenu.Item>
			</DropdownMenu.Group>
			<DropdownMenu.Separator />
			<DropdownMenu.Item class="data-[highlighted]:bg-primary/75" href="{import.meta.env.VITE_PROJECT_REPOSITORY}/issues">
				<Bug class="mr-2 h-4 w-4" />
				<span>Bug Report</span>
			</DropdownMenu.Item>
			<DropdownMenu.Item on:click={() => logout()} class="data-[highlighted]:bg-primary/75">
				<LogOut class="mr-2 h-4 w-4" />
				<span>Log out</span>
			</DropdownMenu.Item>
		</DropdownMenu.Content>
	</DropdownMenu.Root>
{/if}
