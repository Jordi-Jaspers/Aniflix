import {publicPaths} from "@components/Paths";
import {pb} from '@pocketbase/PocketBase';
import {LOGGER} from "@util/Logger";
import {useRouter} from 'next/router';
import {ReactNode, useEffect, useState} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const router = useRouter();
    const [authorized, setAuthorized] = useState<boolean>(false);
    
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
        }
    }
    
    useEffect(() => {
        authCheck(router.asPath);
    }, [router.asPath]);
    
    return (
        <>
            {authorized && children}
        </>
    );
}
