<script lang="ts">
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '$lib/components/ui/card/index.js';
import { Label } from '$lib/components/ui/label/index.js';
import { Input } from '$lib/components/ui/input/index.js';
import { Button } from '$lib/components/ui/button/index.js';
import { useUserDetails } from '$lib/components/store/store.js';
import { toast } from 'svelte-sonner';
import { SERVER_URLS } from '$lib/api/paths';
import { curl } from '$lib/api/client';

let isLoading: boolean;
let isChanged: boolean;
let request: UpdateUserDetailsRequest = { firstName: '', lastName: '' };

async function handleSubmit() {
	isLoading = true;
	request.firstName = request.firstName !== '' ? request.firstName : $useUserDetails.firstName;
	request.lastName = request.lastName !== '' ? request.lastName : $useUserDetails.lastName;
	const response: Response = await curl(SERVER_URLS.USER_DETAILS_PATH, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(request)
	});

	if (response.ok) {
		$useUserDetails.firstName = request.firstName;
		$useUserDetails.lastName = request.lastName;
		request = { firstName: '', lastName: '' };
		toast.success('User details updated successfully.');
	}
	isLoading = false;
}

$: isChanged = request.firstName !== '' || request.lastName !== '';
</script>

<form id="update-profile" on:submit|preventDefault={handleSubmit}>
	<Card>
		<CardHeader class="space-y-1">
			<CardTitle class="text-2xl">Public Profile</CardTitle>
			<div class="space-y-4">
				<CardDescription class="italic">
					Last Login: {new Date($useUserDetails.lastLogin).toLocaleString()}
				</CardDescription>
				<CardDescription>
					Most of the fields on this page are optional and can be deleted at any time, and by filling them out, you're giving us consent to
					share this data wherever your user profile appears.
				</CardDescription>
			</div>
		</CardHeader>
		<CardContent class="space-y-4">
			<div class="grid gap-2">
				<Label>First Name</Label>
				<Input type="text" placeholder={$useUserDetails.firstName} bind:value={request.firstName} autocomplete="given-name" />
			</div>

			<div class="grid gap-2">
				<Label>Last Name</Label>
				<Input type="text" placeholder={$useUserDetails.lastName} bind:value={request.lastName} autocomplete="family-name" />
			</div>
		</CardContent>
		<CardFooter class="my-4">
			<Button form="update-profile" type="submit" class="w-fit" disabled={isLoading || !isChanged}>
				{#if isLoading}
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-e-transparent align-[-0.125em] text-muted-foreground motion-reduce:animate-[spin_1.5s_linear_infinite]"
						role="status"
					/>
				{:else}
					<span>Update profile</span>
				{/if}
			</Button>
		</CardFooter>
	</Card>
</form>
