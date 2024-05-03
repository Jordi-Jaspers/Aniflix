<script>
    import Logo from '$lib/assets/icons/aniflix-logo-large.png';
    import LoginBackground from '$lib/assets/images/aniflix-login.png';
    import LoginCard from '$lib/components/login/login-card.svelte';
    import RegisterCard from '$lib/components/login/register-card.svelte';
    import {Tabs, TabsContent, TabsList, TabsTrigger} from '$lib/components/ui/tabs';
    import {Badge} from "$lib/components/ui/badge/index";
</script>

<div class="container relative grid h-screen flex-col items-center justify-center lg:max-w-none lg:grid-cols-2 lg:px-0">
    <div class="bg-muted relative hidden h-full flex-col p-10 text-white lg:flex dark:border-r">
        <div class="absolute inset-0 min-h-screen min-w-full">
            <img
                    src={LoginBackground}
                    alt={'Authentication'}
                    width={7680}
                    height={4320}
                    class="h-full overflow-hidden object-cover brightness-[0.35]"
                    priority
            />
        </div>

        <div class="relative z-20 flex items-center text-lg font-medium space-x-2">
            <svg class="h-6 w-6 mr-2"
                 xmlns="http://www.w3.org/2000/svg"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="2"
                 stroke-linecap="round"
                 stroke-linejoin="round">
                <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3"/>
            </svg>
            Aniflix.stream
            {#if import.meta.env.VITE_ENV !== 'production'}
                <Badge variant="outline" class="bg-green-600 my-3">
                    TST
                </Badge>
            {/if}
        </div>

        <div class="relative z-20 mt-auto">
            <blockquote class="space-y-2">
                {#if import.meta.env.VITE_ENV !== 'production'}
                    <p class="text-lg">&ldquo;The website is currently in a testing stage. 🔐 &rdquo;</p>
                    <footer class="text-sm">The Developer</footer>
                {:else}
                    <p class="text-lg">&ldquo;I'll find a good quote to put here.&rdquo;</p>
                    <footer class="text-sm">The Developer</footer>
                {/if}
            </blockquote>
        </div>
    </div>
    <div class="lg:p-8 ">
        <div class="mx-auto flex w-[350px] flex-col justify-center space-y-6">
            <div class="px-16">
                <img src={Logo} alt="AniFlix Logo" width={941} height={313} priority/>
            </div>
            <Tabs defaultValue="login">
                <TabsList class="w-full">
                    <TabsTrigger class="w-[50%] text-center" value="login">Login</TabsTrigger>
                    <TabsTrigger class="w-[50%] text-center" value="register">Register</TabsTrigger>
                </TabsList>
                <TabsContent value="login">
                    <LoginCard/>
                </TabsContent>
                <TabsContent value="register">
                    <RegisterCard/>
                </TabsContent>
            </Tabs>
            <p class="text-muted-foreground px-8 text-center text-sm">
                By clicking continue, you agree to our{' '}
                <a href="/terms" class="hover:text-primary underline underline-offset-4"> Terms of Service </a>{' '}
                and{' '}
                <a href="/privacy" class="hover:text-primary underline underline-offset-4"> Privacy Policy </a>
                .
            </p>
        </div>
    </div>
</div>
