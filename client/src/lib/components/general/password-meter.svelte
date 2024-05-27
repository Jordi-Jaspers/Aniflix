<script lang="ts">
	import { writable } from 'svelte/store';
	import { calculatePasswordStrength, getPasswordStrengthLabel } from '$lib/api/util';
	import { Label } from '$lib/components/ui/label';

	export let password = '';
	export let isValidPassword: boolean;

	let strength = writable(0);
	let label = writable('Very Weak');
	let bgColor = 'bg-red-500';
	let textColor = 'text-red-500';

	$: {
		const score = calculatePasswordStrength(password);
		strength.set(score);
		label.set(getPasswordStrengthLabel(score));
		bgColor = score >= 6 ? 'bg-green-500' : score >= 4 ? 'bg-orange-500' : 'bg-red-500';
		textColor = score >= 6 ? 'text-green-500' : score >= 4 ? 'text-orange-500' : 'text-red-500';

		isValidPassword = score === 6;
	}
</script>

{#if password !== ''}
	<div>
		<Label>Password Strength: <span class={textColor}>{$label}</span></Label>
		<div class="mt-1 h-2 rounded bg-gray-200">
			<div class="h-full rounded transition-all duration-300 {bgColor}" style="width: {($strength * 100) / 6}%"></div>
		</div>
	</div>
{/if}
