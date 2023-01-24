import {publicPaths} from "@components/Paths";
import {pb} from '@pocketbase/PocketBase';
import {LOGGER} from "@util/Logger";
import {useRouter} from 'next/router';
import {ReactNode, useEffect, useState} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const router = useRouter();
    const [authorized, setAuthorized] = useState<boolean>(false);
    
    useEffect(() => {
        const handleStart = () => {
            setAuthorized(false);
        };
        const handleComplete = () => {
            setAuthorized(true);
        };
        router.events.on('routeChangeStart', handleStart);
        router.events.on('routeChangeComplete', handleComplete);
        router.events.on('routeChangeError', handleComplete);
        
        return () => {
            router.events.off('routeChangeStart', handleStart);
            router.events.off('routeChangeComplete', handleComplete);
            router.events.off('routeChangeError', handleComplete);
        }
    }, [router]);
    
    useEffect(() => {
        authCheck(router.asPath);
    }, [router.asPath]);
    
    function authCheck(url: string) {
        const root = url.split('?')[0];
        if (!pb.authStore.isValid && !publicPaths.includes(root)) {
            setAuthorized(false);
            router.push({
                pathname: '/login',
                query: {returnUrl: router.asPath}
            }).catch((reason) => LOGGER.error("Failed to redirect to login page:\n" + reason));
        } else {
            setAuthorized(true);
            const params = new URLSearchParams(url.split('?')[1]);
            const returnUrl = params.get('returnUrl');
            if (returnUrl) {
                router.push(returnUrl).catch((reason) => LOGGER.error("Failed to redirect to return url:\n" + reason));
            }
            if (root === "/login") {
                router.push({pathname: '/'}).catch((reason) => LOGGER.error("Failed to redirect to home page:\n" + reason));
            }
        }
    }
    
    return (
        <>
            {authorized && children}
        </>
    );
}
