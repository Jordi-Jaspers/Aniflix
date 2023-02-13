

import LoginForm from "@components/login/LoginForm";
import RegisterForm from "@components/login/RegisterForm";
import VerificationButton from "@components/login/VerificationButton";
import {useIsAuthenticated} from "@hooks/useIsAuthenticated";
import AniFlixLogo from "@icons/AniFlixLogo";
import GithubSignIn from "@icons/GithubSignIn";
import GoogleSignIn from "@icons/GoogleSignIn";
import LoginBackground from "@images/aniflix-login.png";


import React, {useState} from "react";

import Image from "next/image";
import Link from "next/link";

export default function Login() {
    const {isSignedIn, isVerified} = useIsAuthenticated();
    const [register, setRegister] = useState<boolean>(false);
    
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
                        
                        {!isSignedIn && (register
                            ? <h4 className={"font-poppins text-xl text-white"}> Register Account </h4>
                            : <h4 className={"font-poppins text-xl text-white"}> Sign-In </h4>)
                        }
                        
                        {(isSignedIn && !isVerified)
                            ? <VerificationButton/>
                            : register ? <RegisterForm/> : <LoginForm/>
                        }
                        
                        {!isSignedIn &&
                            <div className={"font-poppins text-white flex space-x-2"}>
                                <span className={"interactive-underline cursor-pointer"} onClick={() => setRegister(false)}>Sign-in</span>
                                <span> - </span>
                                <span className={"interactive-underline cursor-pointer"} onClick={() => setRegister(true)}>Register</span>
                            </div>
                        }
                        
                        <div className={"flex flex-col items-center space-y-4 w-full"}>
                            <div className={"w-[80%] h-[1px] bg-[#717171]"}/>
                            <div className={"flex flex-col space-y-4 w-full px-14 items-center"}>
                                <div className={"flex flex-row items-center space-x-4 pb-2"}>
                                    <p className={"text-[#717171] font-poppins text-xs cursor-default"}>
                                        <span> or sign in with </span>
                                    </p>
                                </div>
                                <GoogleSignIn/>
                                <GithubSignIn/>
                                <div className={"flex flex-row items-center space-x-4 pt-10"}>
                                    <Link className={"text-[#717171] font-poppins text-xs interactive-underline"}
                                       href={"https://github.com/Jordi-Jaspers"}>
                                        © Designed and built by Jordi Jaspers
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
