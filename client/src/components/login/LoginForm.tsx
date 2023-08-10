import ErrorAlert from "@components/alerts/ErrorAlert";
import UserService from "@service/UserService";

import React from "react";

import {useForm} from "react-hook-form";


export default function LoginForm() {
    const {register, handleSubmit} = useForm();
    const [isLoading, setIsLoading] = React.useState<boolean>(false);
    const [isError, setIsError] = React.useState<boolean>(false);
    
    async function login(data: any) {
        setIsLoading(true);
        setIsError(await UserService.signIn(data.email, data.password));
        setIsLoading(false);
    }
    
    return (
        <form className={"flex flex-col space-y-4 w-full px-6"} onSubmit={handleSubmit(login)}>
            <div className={"flex flex-col"}>
                <div className={"w-full h-fit bg-[#333333] rounded-md py-2 px-6 flex flex-col mb-4"}>
                    <label className={"text-[#717171] font-poppins text-xs"}>Email Address</label>
                    <input className={"text-white bg-transparent font-poppins pb-[0.25] h-6"} type={"email"}
                           autoComplete={"email"} spellCheck={false} required
                           {...register("email", {required: true})}/>
                </div>
                <div className={"w-full h-fit bg-[#333333] rounded-md py-2 px-6 flex flex-col mb-2 "}>
                    <label className={"text-[#717171] font-poppins text-xs"}>Password</label>
                    <input className={"text-white font-poppins bg-transparent pb-[0.25] h-6"} type={"password"}
                           autoComplete={"password"} spellCheck={false} required minLength={10} maxLength={72}
                           {...register("password", {required: true})}/>
                </div>
                <ErrorAlert message={"Incorrect email address or password."} show={isError}/>
                <button
                    className={"py-4 px-4 mt-2 max-w-md flex justify-center items-center bg-red-600 hover:bg-red-700 text-white w-full transition ease-in duration-200 text-center text-base font-semibold shadow-md rounded-lg"}
                    type={"submit"} disabled={isLoading}>
                    {isLoading ? (
                        <div className={"py-2 px-4 flex space-x-1 justify-center items-center"}>
                            <div className={`h-2 w-2 bg-white rounded-full animate-bounce bounce`}/>
                            <div className={`h-2 w-2 bg-white rounded-full animate-bounce200`}/>
                            <div className={`h-2 w-2 bg-white rounded-full animate-bounce400`}/>
                        </div>
                    ) : (
                        <span className={"text-white font-poppins text-center"}>Sign in</span>
                    )}
                </button>
            </div>
        </form>
    )
}
