"use client"

import OAuthService from "@service/OAuthService";
import {LOGGER} from "@util/Logger";
import {useRouter} from "next/router";
import {AuthProviderInfo} from "pocketbase";
import {useEffect, useState} from "react";

export const useOAuthProvider = (providerName: string): () => void => {
    const router = useRouter();
    const [provider, setProvider] = useState<AuthProviderInfo | undefined>();
    const [redirectUrl, setRedirectUrl] = useState<string>("");
    
    const handleOAuthCallback = () => {
        window.localStorage.setItem("provider", JSON.stringify({provider: provider}));
        router.push(redirectUrl)
            .then(r => LOGGER.debug("[useOAuthProvider] Redirected to: '%s'", r))
            .catch((reason) => LOGGER.error("[useOAuthProvider] Failed to redirect to OAuth provider:\n" + reason));
    }
    
    useEffect(() => {
        OAuthService.getOAuthForProvider(providerName)
            .then((response) => {
                setProvider(response);
                if (response) {
                    const baseUrl = window.location.href.split("?")[0]
                    setRedirectUrl(response?.authUrl + baseUrl + "/oauth");
                }
            });
    }, []);
    
    LOGGER.debug("[useOAuthProvider] Redirect URL has been set to: '%s'", redirectUrl);
    return handleOAuthCallback;
};
