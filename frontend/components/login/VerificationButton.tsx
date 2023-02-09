import ErrorAlert from "@components/alerts/ErrorAlert";
import SuccessAlert from "@components/alerts/SuccessAlert";
import {useUserInformation} from "@hooks/useUserInformation";
import UserService from "@service/UserService";
import React, {useState} from "react";

export default function VerificationButton() {
    const [isError, setIsError] = useState<boolean>(false);
    const [isSuccess, setIsSuccess] = useState<boolean>(false);
    const [message, setMessage] = useState<string>("");
    const {user} = useUserInformation();
    
    async function requestVerification() {
        const response = await UserService.requestVerificationEmail(user?.email);
        setIsError(response.isError);
        setIsSuccess(response.isSuccess);
        setMessage(response.error);
    }
    
    return (
        <div className={"flex flex-col space-y-4 w-full px-6"}>
            <h4 className={"text-white font-poppins text-center text-xl"}>Are you even verified, bro?</h4>
            
            <button
                className={"py-4 px-4 mt-2 w-full flex justify-center items-center bg-red-600 hover:bg-red-700 text-white transition ease-in duration-200 text-center text-base font-semibold shadow-md rounded-lg"}
                onClick={requestVerification} type={"button"} disabled={false}>
                <a className={"text-white font-poppins text-center"}>Resend Verification Email</a>
            </button>
            {isError && <ErrorAlert message={message} show={isError}/>}
            {isSuccess && <SuccessAlert message={message} show={isSuccess}/>}
        </div>
    )
}
