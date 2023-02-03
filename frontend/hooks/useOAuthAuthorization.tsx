import {PRIVATE_PATHS, PUBLIC_PATHS} from "@enum/Paths";
import {pocketBase} from "@pocketbase/PocketBase";
import OAuthService from "@service/OAuthService";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import router, {useRouter} from "next/router";
import {AuthProviderInfo} from "pocketbase";
import {useEffect, useState} from "react";

export const useOAuthAuthorization = () => {
    const router = useRouter();
    const [authData, setAuthData] = useState<any>()
    const {state, code} = router.query;
    
    useEffect(() => {
        if (typeof window !== "undefined") {
            const storage = window.localStorage.getItem("provider");
            const redirect = window.location.origin + PUBLIC_PATHS.LOGIN_OAUTH
            if (!storage || !state || !code) return;
            const provider: AuthProviderInfo = JSON.parse(storage).provider
            if (provider.state !== state) LOGGER.error("[useOAuthAuthorization] State does not match the one from the OAuth client.");
            OAuthService.signInWithOAuth(provider, code as string, redirect);
        }
    }, [state, code]);
};
