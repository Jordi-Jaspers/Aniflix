import '../styles/globals.css'
import Loading from "@components/Loading";
import {useRouter} from "next/router";
import React from "react";
import {RecoilRoot} from 'recoil'
import type {AppProps} from 'next/app'

export default function App({Component, pageProps}: AppProps) {
    const router = useRouter();
    const [loading, setLoading] = React.useState<boolean>(false);
    React.useEffect(() => {
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
            {loading && <div className={"text-9xl"}>Loading....{/*I have an animation here*/}</div>}
            {!loading && <RecoilRoot><Component {...pageProps} /></RecoilRoot>}
        </>
    )
}
