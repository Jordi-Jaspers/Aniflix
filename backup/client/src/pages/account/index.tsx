import Footer from "@components/footer/Footer";
import Header from "@components/header/Header";

import React from "react";

import Head from "next/head";

export default function Account() {
    return (
        <div className={"relative h-full w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Account</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>

            {/*  HEADER  */}
            <Header/>

            {/*  FOOTER  */}
            <Footer/>
        </div>
    )
}
