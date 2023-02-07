import {useUserInformation} from "@hooks/useUserInformation";
import {pocketBase} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import {useQuery} from "react-query";

export default function useIsVerified(isSignedIn: boolean) {
    const id = useUserInformation()?.id;
    
    async function isVerified() {
        let verified = false;
        if (isSignedIn && id != null) {
            verified = await pocketBase.collection("users").getOne(id).then((response) => {
                LOGGER.debug("[UserService] Successfully retrieved user information for user: " + response.id)
                return response.verified;
            }).catch((e: any) => {
                LOGGER.error("[UserService] Failed to retrieve user information for user: " + id, e)
                return false;
            });
        }
        return verified;
    }
    
    return useQuery({queryFn: isVerified, queryKey: ["isVerified", id]})
};

