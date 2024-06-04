<script lang="ts">
    import '../app.pcss';
    import {ModeWatcher} from 'mode-watcher';
    import {Toaster} from "$lib/components/ui/sonner";
</script>

<main>
    <ModeWatcher defaultMode={'dark'}/>
    <slot class="bg-[#1a1920]"/>
    <Toaster />
</main>
