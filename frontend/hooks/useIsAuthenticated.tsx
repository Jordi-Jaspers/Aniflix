import {PRIVATE_PATHS, PUBLIC_PATHS} from "@enum/Paths";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";

export const useIsAuthenticated = () => {
    const router = useRouter();
    const [isSignedIn, setIsSignedIn] = useState<boolean>(false);
    const [isVerified, setIsVerified] = useState<boolean>(false);
    const [isAuthorized, setIsAuthorized] = useState(true);
    
    function authCheck(url: string) {
        if (UserService.isAuthorized(url)) {
            setIsAuthorized(true);
            if (url.split("?")[0] === PUBLIC_PATHS.LOGIN) {
                LOGGER.info("[RouteGuard] Authorized: Redirected user to home page");
                router.push(PRIVATE_PATHS.HOME).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to home page:\n" + reason));
            }
        } else {
            setIsAuthorized(false);
            LOGGER.info("[RouteGuard] Unauthorized: Redirected user to login page with return url: '%s'", router.asPath);
            router.push({
                pathname: PUBLIC_PATHS.LOGIN,
                query: {returnUrl: router.asPath}
            }).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to login page:\n" + reason));
        }
    }
    
    useEffect(() => {
        if (UserService.isAuthenticated()) {
            setIsSignedIn(true);
            LOGGER.debug("[useIsAuthenticated] User is signed-in '%s'", isSignedIn);
    
            const user = UserService.getUserInformation();
            UserService.isVerified(user?.id).then((response) => {
                setIsVerified(response);
                LOGGER.debug("[useIsAuthenticated] User is verified '%s'", isVerified);
            });
        }
    }, [isSignedIn, isVerified]);
    
    useEffect(() => {
        authCheck(router.asPath);
    }, [router.asPath]);
    
    return {isSignedIn, isVerified, isAuthorized};
};
