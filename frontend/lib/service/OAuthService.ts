import {PRIVATE_PATHS, PUBLIC_PATHS} from "@enum/Paths";
import {pocketBase} from "@pocketbase/PocketBase";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import router from "next/router";
import {AuthProviderInfo, RecordAuthResponse} from "pocketbase";

export default class OAuthService {
    
    static async getOAuthMethods(): Promise<AuthProviderInfo[]> {
        const methods = await pocketBase.collection("users").listAuthMethods()
        return methods.authProviders;
    };
    
    static async getOAuthForProvider(provider: string): Promise<AuthProviderInfo | undefined> {
        const methods = await this.getOAuthMethods();
        const oAuthProvider = methods.find((method: AuthProviderInfo) => method.name === provider);
        oAuthProvider
            ? LOGGER.debug("[OAuthService] Requested OAuth Provider has been found: " + oAuthProvider.name)
            : LOGGER.error("[OAuthService] No such OAuth provider is configured: " + provider);
        return oAuthProvider
    };
    
    static async signInWithOAuth(provider: AuthProviderInfo, code: string, redirect: string) {
        LOGGER.debug("[OAuthService] Trying to sign with following details: \n" + provider.name + "\n" + code + "\n" + redirect)
        await pocketBase.collection('users').authWithOAuth2(
            provider.name,
            code,
            provider.codeVerifier,
            redirect
        ).then((authData: RecordAuthResponse) => {
            LOGGER.debug("[OAuthService] Successfully signed in user: \n" + authData);
            UserService.updateLoginDate(authData.record.id);
            UserService.updateLoginDetails(authData.record.id, authData.meta?.rawUser);
            LOGGER.debug("[OAuthService] Successfully updated login date for user: " + JSON.stringify(authData));
            router.push(PRIVATE_PATHS.HOME)
        }).catch((exception: any) => {
            LOGGER.error("[OAuthService] Failed to sign in user with OAuth provider: " + provider.name, exception);
            router.push(PUBLIC_PATHS.LOGIN);
            throw exception;
        });
        window.localStorage.removeItem("provider");
    }
}
