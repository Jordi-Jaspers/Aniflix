import {useOAuthAuthorization} from "@hooks/useOAuthAuthorization";
import AniFlixLogo from "@icons/AniFlixLogo";
import Head from "next/head";
import React from "react";

export default function OAuthRedirect() {
    useOAuthAuthorization();
    return (
        <div className={"bg-black w-screen h-screen flex flex-col space-y-8 justify-center items-center objects-center"}>
            <Head>
                <title>OAuth Redirect</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            <AniFlixLogo/>
            <div className="flex space-x-1">
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce bounce`}/>
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce200`}/>
                <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce400`}/>
            </div>
        </div>
    );
}
