import React from 'react'

import Image from "next/image";
import {useRouter} from "next/router";

type AniFlixLogoProps = {
    className?: string
}

export default function AniFlixLogo({className}: AniFlixLogoProps) {
    const router = useRouter()
    return (
        <Image
            className={className ? className : ""}
            src={"/icons/logo-lg-941x313.png"}
            alt="AniFlix Logo"
            width={313}
            height={100}
            onClick={() => {
                router.push('/')
            }}
        />
    )
}
