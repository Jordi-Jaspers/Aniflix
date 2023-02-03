import {ChevronUpIcon} from "@heroicons/react/24/solid";
import AniFlixLogo from "@icons/AniFlixLogo";
import Link from "next/link";
import {useRouter} from "next/router";
import React, {useState} from 'react'

export default function NavigationMenu() {
    const router = useRouter();
    const [showNavigationMenu, setShowNavigationMenu] = useState(false);
    
    return (
        <>
            <div className="flex flex-row items-center space-x-2 md:space-x-10 h-16">
                <AniFlixLogo className={"cursor-pointer object-contain w-fit h-[2em]"}/>
                <div className={"flex space-x-2 items-center cursor-pointer pl-4"}
                     onMouseEnter={() => {
                         if (window.innerWidth <= 768) {
                             setShowNavigationMenu(true)
                         }
                     }}>
                    <ul className={`space-x-4 flex`}>
                        <li className={`headerLink ${router.pathname === '/' && 'headerLinkActive'}`}>
                            <Link href="/">
                                Home
                            </Link>
                        </li>
                        <li className={`headerLink ${router.pathname === '/trending' && 'headerLinkActive'}`}>
                            <Link href="/trending">
                                Trending
                            </Link>
                        </li>
                        <li className={`headerLink ${router.pathname === '/popular' && 'headerLinkActive'}`}>
                            <Link href="/popular">
                                Popular
                            </Link>
                        </li>
                        <li className={`headerLink ${router.pathname === '/recently_added' && 'headerLinkActive'}`}>
                            <Link href="/recently-added">
                                Recently Added
                            </Link>
                        </li>
                        <li className={`headerLink ${router.pathname === '/favorites' && 'headerLinkActive'}`}>
                            <Link href="/favorites">
                                Favorites
                            </Link>
                        </li>
                    </ul>
                    <ChevronUpIcon className={`${showNavigationMenu && "rotate-180"} w-4 h-4 duration-300 md:hidden`}/>
                    {showNavigationMenu &&
                        <div className="absolute top-12 left-[8em] z-50 rounded-md shadow-md transform bg-black/70 border w-fit"
                             onMouseLeave={() => setShowNavigationMenu(false)}>
                            <ul className={`space-y-1 flex flex-col`}>
                                <li className={"text-white text-sm flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                    <Link href="/">
                                        Home
                                    </Link>
                                </li>
                                <li className={"text-white text-sm flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                    <Link href="/trending">
                                        Trending
                                    </Link>
                                </li>
                                <li className={"text-white text-sm flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                    <Link href="/popular">
                                        Popular
                                    </Link>
                                </li>
                                <li className={"text-white text-sm flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                    <Link href="/recently-added">
                                        Recently Added
                                    </Link>
                                </li>
                                <li className={"text-white text-sm flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                    <Link href="/favorites">
                                        Favorites
                                    </Link>
                                </li>
                            </ul>
                        </div>
                    }
                </div>
            </div>
        </>
    )
}
