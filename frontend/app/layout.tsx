import Loader from "@components/Loader";
import RouteGuard from "@components/RouteGuard";
import '@styles/globals.css'
import ReactQueryWrapper from "@util/ReactQueryWrapper";
import {Head, Html} from "next/document";
import React, {ReactNode} from "react";
import {RecoilRoot} from 'recoil';

interface Props {
    children: ReactNode;
}

export default function RootLayout({children}: Props) {
    return (
        <Html>
            <Head>
                <link rel="manifest" href="/site.webmanifest"/>
                <link rel="icon" type="image/x-icon" href="/favicon.ico"/>
                <link
                    rel="icon"
                    type="image/png"
                    sizes="32x32"
                    href="/icons/favicon-32x32.png"
                />
                <link
                    rel="icon"
                    type="image/png"
                    sizes="16x16"
                    href="/icons/favicon-16x16.png"
                />
                <link
                    rel="apple-touch-icon"
                    sizes="180x180"
                    href="/icons/apple-touch-icon.png"
                />
                <link
                    rel="mask-icon"
                    href="/icons/safari-pinned-tab.svg"
                    color="#5bbad5"
                />
                <title>Aniflix</title>
            </Head>
            <body>
            <ReactQueryWrapper>
                <Loader>
                    <RouteGuard>
                        <RecoilRoot>
                            {children}
                        </RecoilRoot>
                    </RouteGuard>
                </Loader>
            </ReactQueryWrapper>
            </body>
        </Html>
    )
}
