import Loader from "@components/Loader";
import RouteGuard from "@components/RouteGuard";
import '@styles/globals.css'
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
