import Image from "next/image";
import React from 'react'

type AniFlixLogoProps = {
    className?: string
}

export default function AniFlixLogo({className}: AniFlixLogoProps) {
    return (
        <Image
            className={className ? className : ""}
            src={"/icons/logo-lg-941x313.png"}
            alt="AniFlix Logo"
            width={313}
            height={100}
        />
    )
}
