import {PUBLIC_PATHS} from "@enum/Paths";
import {OAuthUser} from "@interfaces/OAuthUser";
import {pocketBase} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import router from "next/router";
import {Admin, Record} from "pocketbase";

export default class UserService {
    
    static isAuthenticated(): boolean {
        return pocketBase.authStore.isValid;
    }
    
    static requestVerificationEmail(email: string): Promise<{ isSuccess: boolean, isError: boolean, error: string }> {
        LOGGER.debug("[UserService] Sending verification email to user: '%s'", email)
        return pocketBase.collection("users").requestVerification(email).then(() => {
            return {isSuccess: true, isError: false, error: "Email sent successfully, Check your email for verification"};
        }).catch((e: any) => {
            return {isSuccess: false, isError: true, error: "Failed to send verification email: " + e.message};
        });
    }
    
    static async signIn(email: string, password: string): Promise<boolean> {
        LOGGER.debug("[UserService] Trying to sign in user.")
        let isError = false;
        try {
            const response = await pocketBase.collection("users").authWithPassword(email, password);
            await this.updateLoginDate(response.record.id);
            await this.redirect()
            LOGGER.debug("[UserService] Login successful for user: " + email);
        } catch (e: any) {
            LOGGER.error("[UserService] Failed to login user '" + email + "'", e);
            isError = true;
        }
        return isError;
    }
    
    static async register(email: string, password: string, confirmPassword: string): Promise<{ isError: boolean, error: string }> {
        LOGGER.debug("[UserService] Trying to register user.")
        
        // Upload file to pocketbase
        const formData = new FormData();
        formData.append("email", email);
        formData.append("emailVisibility", "true");
        formData.append("password", password);
        formData.append("passwordConfirm", confirmPassword);
        formData.append("last_login", new Date().toISOString());
        formData.append("locale", "N/A");
        
        if (password !== confirmPassword) {
            LOGGER.error("[UserService] Failed to register user '" + email + "'", "Passwords do not match");
            return {isError: true, error: "Passwords do not match"};
        }
        return pocketBase.collection("users").create(formData).then(async (response) => {
            await UserService.requestVerificationEmail(response?.email);
            LOGGER.debug("[UserService] Registration successful for user: " + email);
            return {isError: false, error: "Registration successful, Check your email for verification"};
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to register user '" + email + "'", e);
            return {isError: true, error: "Registration failed"};
        })
    }
    
    static async getAvatar(record: Record | Admin | null, file: string): Promise<string> {
        if (record && file) {
            return this.getFileUrl(record, file);
        } else {
            return await pocketBase.collection("avatars").getFullList()
                .then((response) => {
                    const random = Math.floor(Math.random() * response.length);
                    const record = response[random];
                    LOGGER.info("No avatar found for user, using random avatar: " + this.getFileUrl(record, record.avatar));
                    return this.getFileUrl(record, record.avatar);
                }).catch((e: any) => {
                    LOGGER.error("[UserService] Failed to get default avatars", e);
                    return "https://rb.gy/g1pwyx";
                })
        }
    }
    
    static async signOut(): Promise<void> {
        const user = this.getUserInformation();
        LOGGER.debug("[UserService] Trying to sign out user.")
        try {
            pocketBase.authStore.clear()
        } catch (e: any) {
            LOGGER.error("[UserService] Failed to sign out user '" + user?.email + "'", e);
        }
        LOGGER.debug("[UserService] Successfully signed out user: " + user?.email);
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
            LOGGER.debug("[UserService] Successfully updated last login date for user: " + id);
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to update last login date for user: " + id, e);
        });
    }
    
    static async updateAvatar(id: string, avatar: string): Promise<void> {
        await pocketBase.collection("users").update(id, {
            avatar: avatar,
        }).then(() => {
            LOGGER.debug("[UserService] Successfully updated avatar for user: " + id);
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to update avatar for user: " + id, e);
        });
    }
    
    static async updateLoginDetails(id: string, user: OAuthUser, avatar?: string): Promise<void> {
        try {
            if (avatar) await this.updateAvatar(id, avatar);
            await pocketBase.collection("users").update(id, {
                email: user.email,
                given_name: user.given_name,
                family_name: user.family_name,
                locale: (user.locale).toUpperCase(),
                verified: user.verified_email,
            })
            LOGGER.debug("[UserService] Successfully updated account details for user: " + id);
        } catch (e: any) {
            LOGGER.error("[UserService] Failed to update account details for user: " + id, e);
        }
    }
    
    static getUserInformation(): Record | Admin | null {
        LOGGER.debug("[UserService] Retrieving basic user information.")
        return pocketBase.authStore.model;
    }
    
    static async getUserInformationById(id: string): Promise<Record | null> {
        LOGGER.debug("[UserService] Retrieving user information for user: " + id)
        return await pocketBase.collection("users").getOne(id).then((response) => {
            LOGGER.debug("[UserService] Successfully retrieved user information for user: " + response.id)
            return response;
        }).catch((e: any) => {
            LOGGER.error("[UserService] Failed to retrieve user information for user with id '" + id + "':\n" + e.message);
            return null;
        });
    }
    
    static getFileUrl(record: Record | Admin | null, file: string): string {
        LOGGER.debug("[UserService] Retrieving file url ")
        return (record && file) ? pocketBase.getFileUrl(<Record>record, file) : "";
    }
}
