import {PRIVATE_PATHS, PUBLIC_PATHS, PUBLIC_PATHS_LIST} from "@enum/Paths";
import {useIsAuthenticated} from "@hooks/useIsAuthenticated";
import {LOGGER} from "@util/Logger";
import {NextRouter, useRouter} from 'next/router';
import {ReactNode, useEffect, useState} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const router: NextRouter = useRouter();
    const {isSignedIn, isVerified} = useIsAuthenticated()
    const [isAuthorized, setIsAuthorized] = useState(true);
    
    useEffect(() => {
        authCheck(router.asPath);
    }, [router.asPath]);
    
    function authCheck(url: string) {
        const root: string = url.split("?")[0];
        if (!PUBLIC_PATHS_LIST.includes(root) && typeof isSignedIn !== 'undefined' && !isSignedIn && typeof isVerified !== 'undefined' && !isVerified) {
            setIsAuthorized(false);
            LOGGER.info("[RouteGuard] Unauthorized: Redirected user to login page with return url: '%s'", router.asPath);
            router.push({
                pathname: PUBLIC_PATHS.LOGIN,
                query: {returnUrl: router.asPath}
            }).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to login page:\n" + reason));
        } else {
            setIsAuthorized(true);
            if (isSignedIn && isVerified && url.split("?")[0] === PUBLIC_PATHS.LOGIN) {
                LOGGER.info("[RouteGuard] Authorized: Redirected user to home page");
                router.push(PRIVATE_PATHS.HOME).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to home page:\n" + reason));
            }
        }
    }
    
    return (
        <>
            {isAuthorized && children}
        </>
    );
}
