import Loader from "@components/Loader";
import RouteGuard from "@components/RouteGuard";
import '@styles/globals.css'
import {LOGGER} from "@util/Logger";
import type {AppProps} from 'next/app'
import React from "react";
import {QueryClient, QueryClientProvider} from "react-query";
import {RecoilRoot} from 'recoil'

export default function App({Component, pageProps}: AppProps) {
    const queryClient = new QueryClient();
    
    return (
        <QueryClientProvider client={queryClient}>
            <Loader>
                <RouteGuard>
                    <RecoilRoot>
                        <Component {...pageProps} />
                    </RecoilRoot>
                </RouteGuard>
            </Loader>
        </QueryClientProvider>
    )
}

export async function getServerSideProps() {
    LOGGER.info("[PocketBase] Initializing PocketBase with url: " + process.env.NEXT_PUBLIC_POCKETBASE_URL);
    LOGGER.info("[Consumet] Initializing Consumet with url: " + process.env.NEXT_PUBLIC_CONSUMET_BASE_URL);
    LOGGER.info("[Aniflix] Logging is set to: " + process.env.NEXT_PUBLIC_LOGGING_LEVEL);
    
    return {
        props: {}
    }
}
