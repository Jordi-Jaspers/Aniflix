import BasicMenu from "@components/BasicMenu";
import {BellIcon} from '@heroicons/react/24/outline';
import {MagnifyingGlassIcon} from '@heroicons/react/24/solid';
import AniFlixLogo from "@icons/AniFlixLogo";
import Image from "next/image";
import Link from 'next/link'
import {useEffect, useState} from 'react'

export default function Header() {
    const [isScrolled, setIsScrolled] = useState(false)
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
            className={`fixed top-0 bg-gradient-to-b from-[#141414] to-transparent z-[100] h-[70px] w-screen flex flex-row justify-between pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12 transition-colors duration-300 ${isScrolled ? 'bg-[#141414]/80' : 'bg-transparent'}`}>
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
                <Link href="/account">
                    <Image
                        src="https://rb.gy/g1pwyx"
                        alt="Profile Picture"
                        className="cursor-pointer rounded"
                        width={32}
                        height={32}
                    />
                </Link>
            </div>
        </header>
    )
}
