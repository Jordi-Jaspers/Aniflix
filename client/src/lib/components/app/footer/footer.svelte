<script lang="ts">
import {ChangelogDialog} from "$lib/components/changelog";

let applicationVersion = __VERSION__;
let applicationName = __NAME__;
</script>

<footer class="px-[4%] py-10 text-center text-xs text-muted-foreground">
	<div>
		<span>{applicationName}</span>
		<span>|</span>
		<ChangelogDialog bind:applicationVersion/>
		<span>|</span>
		<span>© <a href="https://github.com/Jordi-Jaspers" target="_blank" class="hover:underline"> Jordi Jaspers</a> 2024, All rights reserved.</span>
	</div>
</footer>
