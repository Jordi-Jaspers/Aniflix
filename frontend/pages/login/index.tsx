import ErrorAlert from "@components/alerts/ErrorAlert";
import AniFlixLogo from "@icons/AniFlixLogo";
import GithubSignIn from "@icons/GithubSignIn";
import GoogleSignIn from "@icons/GoogleSignIn";
import LoginBackground from "@images/aniflix-login.png";
import UserService from "@service/UserService";
import Image from "next/image";
import React from "react";
import {useForm} from "react-hook-form";

export default function Login() {
    const [isLoading, setIsLoading] = React.useState<boolean>(false);
    const [isError, setIsError] = React.useState<boolean>(false);
    const {register, handleSubmit} = useForm();
    
    async function login(data: any) {
        setIsLoading(true);
        setIsError(await UserService.signIn(data.email, data.password));
        setIsLoading(false);
    }
    
    return (
        <div className={"h-full w-full max-w-[550px] mx-auto flex justify-center items-center"}>
            <div className={"absolute top-0 left-0 w-full h-full brightness-75 -z-10"}>
                <Image
                    src={LoginBackground}
                    alt={"Login Background"}
                    width={7680}
                    height={4320}
                    className={"min-h-screen h-full object-cover overflow-hidden"}
                    priority
                />
            </div>
            
            <div className={"flex items-center min-h-[100vh] w-full p-[5%]"}>
                <div className={"bg-black/90 flex flex-col w-full p-10 mb-8 rounded-lg"}>
                    <div className={"flex flex-col items-center space-y-6 mb-6"}>
                        <AniFlixLogo className={"w-[50%] h-[50%}"}/>
                        <form className={"flex flex-col space-y-4 w-full py-8 px-6"} onSubmit={handleSubmit(login)}>
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
                                           autoComplete={"password"} spellCheck={false} required
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
                                        <a className={"text-white font-poppins text-center"}>Sign in</a>
                                    )}
                                </button>
                            </div>
                        </form>
                        
                        <div className={"flex flex-col items-center space-y-4 w-full"}>
                            <div className={"w-[80%] h-[1px] bg-[#717171]"}/>
                            <div className={"flex flex-col space-y-4 w-full px-14 items-center"}>
                                <div className={"flex flex-row items-center space-x-4 pb-2"}>
                                    <a className={"text-[#717171] font-poppins text-xs"}>Or sign in with</a>
                                </div>
                                <GoogleSignIn/>
                                <GithubSignIn/>
                                <div className={"flex flex-row items-center space-x-4 pt-10"}>
                                    <a className={"text-[#717171] font-poppins text-xs interactive-underline"}
                                       href={"https://github.com/Jordi-Jaspers"}>
                                        © Designed and built by Jordi Jaspers
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
