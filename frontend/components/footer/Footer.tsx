import Link from "next/link";
import React from "react";

export default function Footer() {
    return (
        <footer className={"flex flex-row items-center justify-center p-8 w-full bg-[#1E1E25]"}>
            <Link className={"font-poppins text-[#d1d1d1]"} href={"https://github.com/Jordi-Jaspers"}>
                © Designed and built by Jordi Jaspers
            </Link>
        </footer>
    )
}
