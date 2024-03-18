<script lang="ts">
	import { Icons } from '$lib/components/icons';
	import { Button } from '$lib/components/ui/button';
</script>

<div class="grid grid-cols-2 gap-6">
	<Button class="space-x-2" variant="outline" disabled>
		<Icons.gitHub class="h-6" />
		<span> Github </span>
	</Button>

	<Button class="space-x-2" variant="outline" disabled>
		<Icons.google class="h-5" />
		<span> Google </span>
	</Button>
</div>
