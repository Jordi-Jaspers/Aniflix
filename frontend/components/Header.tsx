import BasicMenu from "@components/BasicMenu";
import {BellIcon, CogIcon, QuestionMarkCircleIcon, UserIcon} from '@heroicons/react/24/outline';
import {MagnifyingGlassIcon} from '@heroicons/react/24/solid';
import AniFlixLogo from "@icons/AniFlixLogo";
import {MediaSource} from "@interfaces/MediaSource";
import {pb} from "@pocketbase/PocketBase";
import {LOGGER} from "@util/Logger";
import Image from "next/image";
import Link from 'next/link'
import React, {useEffect, useState} from 'react'

export default function Header() {
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [isScrolled, setIsScrolled] = useState(false)
    
    const handleLogout = () => {
        const user = pb.authStore.model;
        try {
            pb.authStore.clear()
            window.location.reload()
        }
        catch (e) {
            LOGGER.error("[LOGOUT HANDLER] Failed to logout user '" + user + "'", e);
        }
        LOGGER.info("[LOGOUT HANDLER] logout successful for user: " + user);
    }
    
    useEffect(() => {
        const handleScroll = () => {
            if (window.scrollY > 0) {
                setIsScrolled(true)
            } else {
                setIsScrolled(false)
            }
        }
        window.addEventListener('scroll', handleScroll)
        return () => {
            window.removeEventListener('scroll', handleScroll)
        }
    }, [])
    
    return (
        <header
            className={`sticky top-0 z-[100] h-[70px] w-screen flex flex-row justify-between pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12
            bg-gradient-to-b from-[#141414] to-transparent transition-colors duration-300
            ${isScrolled ? 'bg-[#141414]/80' : 'bg-transparent'}`}>
            <div className="flex flex-row items-center space-x-2 md:space-x-10 h-16">
                <AniFlixLogo className={"cursor-pointer object-contain w-fit h-[2em]"}/>
                <BasicMenu/>
                
                <ul className="hidden space-x-4 md:flex">
                    <li className="headerLink cursor-default font-semibold text-white hover:text-white">
                        Home
                    </li>
                    <li className="headerLink">TV Shows</li>
                    <li className="headerLink">Movies</li>
                    <li className="headerLink">Trending</li>
                    <li className="headerLink">New & Popular</li>
                    <li className="headerLink">Recently Added</li>
                </ul>
            </div>
            <div className="flex flex-row items-center space-x-6 text-sm font-poppins">
                <MagnifyingGlassIcon className="headerIcon"/>
                <BellIcon className="headerIcon"/>
                <div onMouseEnter={() => setShowAccountMenu(true)}>
                    <Link href="">
                        <Image
                            src="https://rb.gy/g1pwyx"
                            alt="Profile Picture"
                            className="cursor-pointer rounded"
                            width={32}
                            height={32}
                        />
                    </Link>
                    {showAccountMenu && (
                        <div className="absolute z-100 top-16 right-12 rounded-md shadow-md transform bg-black/70 border w-[15%]"
                        onMouseLeave={() => setShowAccountMenu(false)}>
                            <ul className="flex flex-col w-full">
                                <Link href="" className={"text-white flex flex-row justify-start items-center py-2 px-4"}>
                                    <UserIcon className={"h-8 w-8"}/>
                                    <p className={"font-poppins p-4 hover:underline"}>Account</p>
                                </Link>
                                <Link href="" className={"text-white flex flex-row justify-start items-center py-2 px-4"}>
                                    <CogIcon className={"h-8 w-8"}/>
                                    <p className={"font-poppins p-4 hover:underline"}>Settings</p>
                                </Link>
                                <Link href="" className={"text-white flex flex-row justify-start items-center py-2 px-4"}>
                                    <QuestionMarkCircleIcon className={"h-8 w-8"}/>
                                    <p className={"font-poppins p-4 hover:underline"}>Help</p>
                                </Link>
                            </ul>/
                            <div className={"w-full h-[1px] bg-white"}/>
                            <div className={"h-full w-full"}>
                                <button className={"w-full font-poppins text-white p-4 hover:underline"} onClick={() => handleLogout()}>
                                    Log Out
                                </button>
                            </div>
                        </div>
                    )}
                </div>

            </div>
        </header>
    )
}
