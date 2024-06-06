<script lang="ts">
import { writable } from 'svelte/store';
import { calculatePasswordStrength, getPasswordStrengthLabel } from '$lib/api/util';
import { Label } from '$lib/components/ui/label';

export let password = '';
export let confirmation = '';
export let isValid: boolean = false;

let strength = writable(0);
let label = writable('Very Weak');
let bgColor = 'bg-red-500';
let textColor = 'text-red-500';
let missingRequirements = writable<string[]>([]);

$: {
	const { score, missing } = calculatePasswordStrength(password);
	const isMatch: boolean = password === confirmation;
	if (!isMatch) {
		missing.push('Password should match confirmation');
	}

	strength.set(score);
	label.set(getPasswordStrengthLabel(score));
	missingRequirements.set(missing);
	bgColor = score >= 7 ? 'bg-green-500' : score >= 4 ? 'bg-orange-500' : 'bg-red-500';
	textColor = score >= 7 ? 'text-green-500' : score >= 4 ? 'text-orange-500' : 'text-red-500';

	isValid = score === 7 && isMatch;
}
</script>

{#if password !== ''}
	<div>
		<Label>Password Strength: <span class={textColor}>{$label}</span></Label>
		<div class="mt-1 h-2 rounded bg-gray-200">
			<div class="h-full rounded transition-all duration-300 {bgColor}" style="width: {($strength * 100) / 7}%"></div>
		</div>
		{#if $missingRequirements.length > 0}
			<div class="mt-2 text-sm text-red-500">
				<ul class="list-inside list-disc">
					{#each $missingRequirements as requirement}
						<li>{requirement}</li>
					{/each}
				</ul>
			</div>
		{/if}
	</div>
{/if}
