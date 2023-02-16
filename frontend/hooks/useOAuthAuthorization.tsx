"use client"

import {PUBLIC_PATHS} from "@enum/Paths";
import OAuthService from "@service/OAuthService";
import {LOGGER} from "@util/Logger";
import {useRouter} from "next/router";
import {AuthProviderInfo} from "pocketbase";
import {useEffect} from "react";

export const useOAuthAuthorization = () => {
    const router = useRouter();
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
