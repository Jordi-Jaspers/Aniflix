import ErrorAlert from "@components/alerts/ErrorAlert";
import UserService from "@service/UserService";
import React from "react";
import {useForm} from "react-hook-form";

interface Props {
    onClick: () => void;
}

export default function VerificationButton({onClick}: Props) {
    return (
        <div className={"flex flex-col space-y-4 w-full px-6"}>
            <h4 className={"text-white font-poppins text-center text-xl"}>Are you even verified, bro?</h4>
            
            <button
                className={"py-4 px-4 mt-2 w-full flex justify-center items-center bg-red-600 hover:bg-red-700 text-white transition ease-in duration-200 text-center text-base font-semibold shadow-md rounded-lg"}
                onClick={onClick} type={"button"} disabled={false}>
                <a className={"text-white font-poppins text-center"}>Resend Verification Email</a>
            </button>
        </div>
    )
}
