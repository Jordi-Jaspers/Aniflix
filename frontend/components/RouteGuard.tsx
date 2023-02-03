import {PRIVATE_PATHS, PUBLIC_PATHS} from "@enum/Paths";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import {NextRouter, useRouter} from 'next/router';
import {ReactNode, useEffect, useState} from 'react';

export default function RouteGuard({children}: { children: ReactNode }) {
    const router: NextRouter = useRouter();
    const [isAuthorized, setIsAuthorized] = useState(true);
    
    useEffect(() => {
        authCheck(router.asPath);
    }, [router.asPath]);
    
    function authCheck(url: string) {
        if (!UserService.isAuthorized(url)) {
            setIsAuthorized(false);
            LOGGER.debug("[RouteGuard] Unauthorized: Redirected user to login page with return url: '%s'", router.asPath);
            router.push({
                pathname: PUBLIC_PATHS.LOGIN,
                query: {returnUrl: router.asPath}
            }).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to login page:\n" + reason));
        } else {
            setIsAuthorized(true);
            if (UserService.isAuthenticated() && url === PUBLIC_PATHS.LOGIN) {
                LOGGER.debug("[RouteGuard] Authorized: Redirected user to home page");
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
