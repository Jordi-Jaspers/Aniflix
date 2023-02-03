import {PUBLIC_PATHS, PUBLIC_PATHS_LIST} from "@enum/Paths";
import {OAuthUser} from "@interfaces/OAuthUser";
import {pocketBase} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import router from "next/router";
import {Admin, Record} from "pocketbase";

export default class UserService {
    
    static isAuthenticated(): boolean {
        return pocketBase.authStore.isValid
    }
    
    static isAuthorized(path: string): boolean {
        LOGGER.debug("[UserService] Verifying if the user is authorized to access: '%s'", path)
        const rootPath = path.split('?')[0];
        const isValid: boolean = this.isAuthenticated() || PUBLIC_PATHS_LIST.includes(rootPath)
        LOGGER.debug("[UserService] User with email '%s' is authorized: '%s'", this.getUserInformation()?.email, isValid)
        return isValid
    }
    
    static async signIn(email: string, password: string): Promise<boolean> {
        LOGGER.info("[UserService] Trying to sign in user.")
        let isError = false;
        try {
            const response = await pocketBase.collection("users").authWithPassword(email, password);
            await this.updateLoginDate(response.record.id);
            await this.redirect()
            LOGGER.info("[UserService] Login successful for user: " + email);
        } catch (e: any) {
            LOGGER.error("[UserService] Failed to login user '" + email + "'", e);
            isError = true;
        }
        return isError;
    }
    
    static async signOut(): Promise<void> {
        const user = this.getUserInformation();
        LOGGER.info("[UserService] Trying to sign out user.")
        try {
            pocketBase.authStore.clear()
        } catch (e: any) {
            LOGGER.error("[UserService] Failed to sign out user '" + user?.email + "'", e);
        }
        LOGGER.info("[UserService] Successfully signed out user: " + user?.email);
        await this.redirect()
    }
    
    static async redirect(): Promise<void> {
        try {
            if (this.isAuthenticated()) {
                router.query.returnUrl ? await router.push(router.query.returnUrl.toString()) : await router.push('/');
            } else {
                await router.push({
                    pathname: PUBLIC_PATHS.LOGIN,
                    query: {returnUrl: router.asPath}
                });
            }
        } catch (e) {
            LOGGER.error("[UserService] Failed to redirect user.");
        }
    }
    
    static async updateLoginDate(id: string): Promise<void> {
        await pocketBase.collection("users").update(id, {
            last_login: new Date(),
        }).then(() => {
            LOGGER.info("[UserService] Successfully updated last login date for user: " + id);
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to update last login date for user: " + id, e);
        });
    }
    
    static async updateLoginDetails(id: string, user: OAuthUser): Promise<void> {
        await pocketBase.collection("users").update(id, {
            email: user.email,
            given_name: user.given_name,
            family_name: user.family_name,
            locale: user.locale,
            verified: user.verified_email,
        }).then(() => {
            LOGGER.info("[UserService] Successfully updated account details for user: " + id);
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to update account details for user: " + id, e);
        });
    }
    
    static getUserInformation(): Record | Admin | null {
        LOGGER.debug("[UserService] Retrieving user information.")
        return pocketBase.authStore.model;
    }
    
    static getFileUrl(record: Record | Admin | null, file: string): string {
        LOGGER.info("[UserService] Retrieving file url for user: '%s'", pocketBase.authStore.model?.avatar)
        return (record && file) ? pocketBase.getFileUrl(<Record>record, file) : "https://rb.gy/g1pwyx";
    }
}
