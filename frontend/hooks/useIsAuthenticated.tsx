"use client"

import {PRIVATE_PATHS, PUBLIC_PATHS, PUBLIC_PATHS_LIST} from "@enum/Paths";
import useIsVerified from "@hooks/useIsVerified";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";

export const useIsAuthenticated = () => {
    const router = useRouter();
    const [isAuthorized, setIsAuthorized] = useState(false);
    const isSignedIn = UserService.isAuthenticated();
    const {data: isVerified} = useIsVerified(isSignedIn);
    
    function authCheck(url: string) {
        const rootPath = url.split('?')[0];
        LOGGER.debug("[UserService] Verifying if the user is authorized to access: '%s'", url)
        if (PUBLIC_PATHS_LIST.includes(rootPath) || (isSignedIn && isVerified)) {
            setIsAuthorized(true);
            if (url.split("?")[0] === PUBLIC_PATHS.LOGIN && isSignedIn && isVerified) {
                LOGGER.debug("[RouteGuard] Authorized: Redirected user to home page");
                router.push(PRIVATE_PATHS.HOME).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to home page:\n" + reason));
            }
        } else {
            setIsAuthorized(false);
            LOGGER.debug("[RouteGuard] Unauthorized: Redirected user to login page with return url: '%s'", router.asPath);
            router.push({
                pathname: PUBLIC_PATHS.LOGIN,
                query: {returnUrl: router.asPath}
            }).catch((reason) => LOGGER.error("[RouteGuard] Failed to redirect to login page:\n" + reason));
        }
    }
    
    useEffect(() => {
        if (typeof isVerified !== "undefined") {
            authCheck(router.asPath);
            LOGGER.debug("[UserService] User is authorized: '%s'", isAuthorized);
        }
    }, [router.asPath, isSignedIn, isVerified]);
    
    return {isSignedIn, isVerified, isAuthorized};
};
