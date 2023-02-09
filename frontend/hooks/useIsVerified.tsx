import {useUserInformation} from "@hooks/useUserInformation";
import UserService from "@service/UserService";
import {useQuery} from "react-query";

export default function useIsVerified(isSignedIn: boolean) {
    const {id} = useUserInformation();
    
    async function isVerified() {
        let verified = false;
        if (isSignedIn && id != null) {
            verified = await UserService.getUserInformationById(id).then((response) => {
                return response ? response.verified : false;
            }).catch((e: any) => {
                return false;
            });
        }
        return verified;
    }
    
    return useQuery({queryFn: isVerified, queryKey: ["isVerified", id]})
};

