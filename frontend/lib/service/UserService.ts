import {PUBLIC_PATHS, PUBLIC_PATHS_LIST} from "@enum/Paths";
import {OAuthUser} from "@interfaces/OAuthUser";
import {pocketBase} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import router from "next/router";
import {Admin, Record} from "pocketbase";

export default class UserService {
    
    static isAuthenticated(): boolean {
        return pocketBase.authStore.isValid;
    }
    
    static async isVerified(id: string | undefined): Promise<boolean> {
        if (id === undefined) {
            LOGGER.debug("[UserService] id must be provided to verify user.");
            return false;
        }
        
        let isVerified: boolean;
        const user: Record | null = await this.getUserInformationById(id);
        isVerified = user?.verified;
        LOGGER.debug("[UserService] User with email '%s' is verified: '%s'", user?.email, isVerified);
        return isVerified;
    }
    
    static isAuthorized(path: string): boolean {
        LOGGER.info("[UserService] Verifying if the user is authorized to access: '%s'", path)
        const rootPath = path.split('?')[0];
        let isValid: boolean = false;
        if (PUBLIC_PATHS_LIST.includes(rootPath) || (this.isAuthenticated() && this.isVerified(this.getUserInformation()?.id))) {
            isValid = true;
        }
        LOGGER.info("[UserService] User with email '%s' is authorized: '%s'", this.getUserInformation()?.email, isValid)
        return isValid
    }
    
    static requestVerificationEmail(email: string): Promise<{isSuccess: boolean, isError: boolean, error: string }> {
        LOGGER.info("[UserService] Sending verification email to user: '%s'", email)
        return pocketBase.collection("users").requestVerification(email).then(() => {
            return {isSuccess: true, isError: false, error: "Email sent successfully, Check your email for verification"};
        }).catch((e: any) => {
            return {isSuccess: false, isError: true, error: "Failed to send verification email: " + e.message};
        });
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
    
    static async register(email: string, password: string, confirmPassword: string): Promise<{ isError: boolean, error: string }> {
        LOGGER.info("[UserService] Trying to register user.")
        const data = {
            "email": email,
            "emailVisibility": true,
            "password": password,
            "passwordConfirm": confirmPassword,
            "last_login": new Date(),
            "locale": "N/A",
        };
        
        if (password !== confirmPassword) {
            LOGGER.error("[UserService] Failed to register user '" + email + "'", "Passwords do not match");
            return {isError: true, error: "Passwords do not match"};
        }
        return pocketBase.collection("users").create(data).then(async (response) => {
            await UserService.requestVerificationEmail(response?.email);
            LOGGER.info("[UserService] Registration successful for user: " + email);
            return {isError: false, error: "Registration successful, Check your email for verification"};
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to register user '" + email + "'", e);
            return {isError: true, error: "Registration failed"};
        })
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
            locale: (user.locale).toUpperCase(),
            verified: user.verified_email,
        }).then(() => {
            LOGGER.info("[UserService] Successfully updated account details for user: " + id);
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to update account details for user: " + id, e);
        });
    }
    
    static getUserInformation(): Record | Admin | null {
        LOGGER.debug("[UserService] Retrieving basic user information.")
        return pocketBase.authStore.model;
    }
    
    static async getUserInformationById(id: string): Promise<Record | null> {
        LOGGER.debug("[UserService] Retrieving user information for user: " + id)
        return await pocketBase.collection("users").getOne(id).then((response) => {
            LOGGER.info("[UserService] Successfully retrieved user information for user: " + response.id)
            return response;
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to retrieve user information for user: " + id, e)
            return null;
        });
    }
    
    static getFileUrl(record: Record | Admin | null, file: string): string {
        LOGGER.info("[UserService] Retrieving file url for user: '%s'", pocketBase.authStore.model?.avatar)
        return (record && file) ? pocketBase.getFileUrl(<Record>record, file) : "https://rb.gy/g1pwyx";
    }
}
