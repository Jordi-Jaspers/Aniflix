import {ChevronUpIcon} from "@heroicons/react/24/solid";
import AniFlixLogo from "@icons/AniFlixLogo";
import Link from "next/link";
import {useRouter} from "next/router";
import React, {useState} from "react";

const pages = [
    {name: "Home", path: "/"},
    {name: "Trending", path: "/trending"},
    {name: "Popular", path: "/popular"},
    {name: "My Library", path: "/library"},
];

export default function NavigationMenu() {
    const router = useRouter();
    const [showNavigationMenu, setShowNavigationMenu] = useState(false);
    
    return (
        <div className="flex flex-row items-center w-fit">
            <AniFlixLogo className="cursor-pointer object-contain w-fit h-[2em]"/>
            <div className="flex space-x-2 items-center cursor-pointer pl-4">
                <ul className="space-x-4 flex">
                    {pages.map(({name, path}) => (
                        <li key={path} className={`headerLink ${router.pathname === path ? "headerLinkActive" : ""}`}>
                            <Link href={path}>{name}</Link>
                        </li>
                    ))}
                </ul>
                <ChevronUpIcon className={`${showNavigationMenu && "rotate-180"} w-4 h-4 duration-300 md:hidden`}
                               onMouseEnter={() => setShowNavigationMenu(true)}
                />
                {showNavigationMenu && (
                    <div className={"absolute left-0 z-100 bg-transparent h-full w-fit h-full"}
                         onMouseLeave={() => setShowNavigationMenu(false)}>
                        <div className="relative top-[4.4em] rounded-md shadow-md transform bg-black/70 border w-[200px]">
                            <ul className="flex flex-col w-full text-white text-sm">
                                {pages.map(({name, path}) => (
                                    <li key={path}
                                        className="text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded">
                                        <Link href={path}>{name}</Link>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
