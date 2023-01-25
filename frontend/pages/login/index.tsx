import AniFlixLogo from "@icons/AniFlixLogo";
import {pb} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import React from "react";
import {useForm} from "react-hook-form";

export default function Login() {
    const [isLoading, setIsLoading] = React.useState<boolean>(false);
    const {register, handleSubmit} = useForm();
    
    async function login(data: any) {
        setIsLoading(true);
        try {
            await pb.collection("users").authWithPassword(data.email, data.password);
        } catch (e: any) {
            LOGGER.error("[LOGIN PAGE] Failed to login user '" + data.email + "'", e);
        }
        setIsLoading(false);
        LOGGER.info("[LOGIN PAGE] Login successful for user: " + data.email);
    }
    
    return (
        <div className={"h-full w-full max-w-[550px] mx-auto flex justify-center items-center"}>
            <div className={"absolute bg-white object-cover h-full min-h-full overflow-hidden w-full -z-10"}>
                {/*background*/}
            </div>
            
            <div className={"flex items-center min-h-[100vh] w-full p-[5%]"}>
                <div className={"bg-black flex flex-col w-full p-10 mb-8 rounded-lg"}>
                    <div className={"flex flex-col items-center space-y-6 mb-6"}>
                        <AniFlixLogo className={"w-[50%] h-[50%}"}/>
                        <form className={"flex flex-col space-y-4 w-full py-2 px-6"} onSubmit={handleSubmit(login)}>
                            <div className={"flex flex-col"}>
                                <div className={"w-full h-fit bg-[#333333] rounded-md py-2 px-6 flex flex-col mb-4"}>
                                    <label className={"text-[#717171] font-poppins text-xs"}>Email Address</label>
                                    <input className={"text-white bg-transparent font-poppins pb-[0.25] h-6"} type={"email"}
                                           autoComplete={"email"} spellCheck={false}
                                           {...register("email", {required: true})}/>
                                </div>
                                <div className={"w-full h-fit bg-[#333333] rounded-md py-2 px-6 flex flex-col mb-4"}>
                                    <label className={"text-[#717171] font-poppins text-xs"}>Password</label>
                                    <input className={"text-white font-poppins bg-transparent pb-[0.25] h-6"} type={"password"}
                                           autoComplete={"password"} spellCheck={false}
                                           {...register("password", {required: true})}/>
                                </div>
                                <button className={"block bg-red-600 rounded-md items-center h-12 mt-6"} type={"submit"}
                                        disabled={isLoading}>
                                    {isLoading ? (
                                        <div className={"flex space-x-1 justify-center items-center"}>
                                            <div className={`h-2 w-2 bg-black rounded-full animate-bounce bounce`}/>
                                            <div className={`h-2 w-2 bg-black rounded-full animate-bounce200`}/>
                                            <div className={`h-2 w-2 bg-black rounded-full animate-bounce400`}/>
                                        </div>
                                    ) : (
                                        <a className={"text-white font-poppins text-center"}>Log in</a>
                                    )}
                                </button>
                            </div>
                        </form>
                        
                        <div className={"flex flex-col items-center space-y-4 w-full"}>
                            <div className={"w-[80%] h-[1px] bg-[#717171]"}/>
                            <div className={"flex flex-col space-y-4 w-full py-2 px-14 items-center"}>
                                <div className={"flex flex-row items-center space-x-4"}>
                                    <a className={"text-[#717171] font-poppins text-xs"}>Or sign up with</a>
                                </div>
                                <button className={"block bg-[#333333] rounded-md items-center h-12 w-[80%]"}>
                                    <a className={"text-white font-poppins text-center"}>Google</a>
                                </button>
                                <button className={"block bg-[#333333] rounded-md items-center h-12 w-[80%]"}>
                                    <a className={"text-white font-poppins text-center"}>Github</a>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
