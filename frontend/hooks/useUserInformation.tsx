import {pocketBase} from "@pocketbase/PocketBase";
import UserService from "@service/UserService";
import {Admin, Record} from "pocketbase";
import {useEffect, useState} from "react";

export const useUserInformation = () => {
    const [user, setUser] = useState<Record | Admin | null>(null);
    useEffect(() => {
        const user = UserService.getUserInformation();
        if (user) {
            setUser(user);
        }
    }, []);
    
    return user;
};
