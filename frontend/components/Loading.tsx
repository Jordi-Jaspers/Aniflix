import {useRouter} from "next/router";
import React from "react";

export default function Loading() {
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
            {loading && <div>Loading....{/*I have an animation here*/}</div>}
        </>
    )
}
