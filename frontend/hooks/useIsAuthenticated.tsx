import {pocketBase} from "@pocketbase/PocketBase";
import UserService from "@service/UserService";
import {LOGGER} from "@util/Logger";
import {Admin, Record} from "pocketbase";
import {useEffect, useState} from "react";

export const useIsAuthenticated = () => {
    const [isSignedIn, setIsSignedIn] = useState<boolean>();
    const [isVerified, setIsVerified] = useState<boolean>();
    const [user, setUser] = useState<Record | Admin | null>(null);
    const [error, setError] = useState<boolean>();
    
    useEffect(() => {
        if (UserService.isAuthenticated()){
            setIsSignedIn(true);
            checkVerification().catch((reason) => console.error(reason));
            LOGGER.debug("[useIsAuthenticated] User is signed-in '%s' and verified '%s'", isSignedIn, isVerified);
        }
    }, [isSignedIn, isVerified]);
    
    async function checkVerification() {
        const record = pocketBase.authStore.model
        setUser(record);
        
        const userData = await pocketBase.collection("users").getOne(record?.id as string);
        setIsVerified(userData.verified);
    }
    
    async function requestVerification() {
        const response = await UserService.requestVerificationEmail(user?.email);
        response ? setError(false) : setError(true);
    }

    return {isSignedIn, isVerified, requestVerification, error};
};
