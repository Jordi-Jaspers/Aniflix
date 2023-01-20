import Loader from "@components/Loader";
import type {AppProps} from 'next/app'
import {useRouter} from "next/router";
import React from "react";
import {RecoilRoot} from 'recoil'
import '../styles/globals.css'

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
            {loading && (<Loader/>)}
            {!loading && <RecoilRoot><Component {...pageProps} /></RecoilRoot>}
        </>
    )
}
