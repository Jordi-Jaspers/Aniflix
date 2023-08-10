"use client"

import AniFlixLogo from "@icons/AniFlixLogo";

import type {ReactNode} from 'react';
import React, { useEffect, useState} from 'react'

import {useRouter} from "next/router";


/**
 * Deprecated for now, will be used in the future
 */
export default function Loader({children}: { children: ReactNode }) {
    const [loading, setLoading] = useState<boolean>(false);
    const router = useRouter();
    
    useEffect(() => {
        const handleStart = () => {
            setLoading(true);
        };
        const handleComplete = () => {
            setLoading(false);
        };
        router.events.on('routeChangeStart', handleStart);
        router.events.on('routeChangeComplete', handleComplete);
        router.events.on('routeChangeError', handleComplete);
    }, [router]);
    
    return (
        <>
            {loading ? (
                <div className={"bg-black w-screen h-screen flex flex-col space-y-8 justify-center items-center objects-center"}>
                    <AniFlixLogo/>
                    <div className="flex space-x-1">
                        <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce bounce`}/>
                        <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce200`}/>
                        <div className={`h-4 w-4 bg-red-600 rounded-full animate-bounce400`}/>
                    </div>
                </div>
            ) : (
                children
            )}
        </>
    )
}
