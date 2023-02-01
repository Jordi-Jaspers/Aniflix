import AccountMenu from "@components/header/AccountMenu";
import NavigationMenu from "@components/header/NavigationMenu";
import SearchBar from "@components/header/search/SearchBar";
import {BellIcon} from '@heroicons/react/24/outline';
import {useIsScrolled} from "@hooks/useIsScrolled";
import React from 'react'

export default function Header() {
    const isScrolled = useIsScrolled();
    return (
        <header
            className={`sticky top-0 z-[100] h-[70px] w-screen flex flex-row justify-between pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 lg:pr-12
            bg-gradient-to-b from-[#141414] to-transparent transition-colors duration-300
            ${isScrolled ? 'bg-[#141414]/80' : 'bg-transparent'}`}>
            <NavigationMenu/>
            <div className="flex flex-row items-center space-x-6 text-sm font-poppins">
                <SearchBar/>
                <BellIcon className="headerIcon"/>
                <AccountMenu/>
            </div>
        </header>
    )
}
