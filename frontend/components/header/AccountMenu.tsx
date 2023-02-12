import {useUserInformation} from "@hooks/useUserInformation";
import UserService from "@service/UserService";
import CoffeeIcon from "@svg/Coffee.svg";

import React, {useState} from 'react'

import {ArrowLeftOnRectangleIcon, CogIcon, QuestionMarkCircleIcon, UserIcon} from "@heroicons/react/24/outline";
import {ChevronUpIcon} from "@heroicons/react/24/solid";
import Image from "next/image";
import Link from "next/link";


const DONATION_URL: string = process.env.NEXT_PUBLIC_STRIPE_LINK ? process.env.NEXT_PUBLIC_STRIPE_LINK : ""
export default function AccountMenu() {
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const {avatar} = useUserInformation();
    
    return (
        <div className={"flex flex-row justify-center w-[64px] h-[32px] items-center"}
             onClick={() => setShowAccountMenu(!showAccountMenu)}
             onMouseEnter={() => {setShowAccountMenu(true)}}>
            <div className="flex flex-row items-center space-x-2" >
                <Image
                    src={avatar}
                    alt="Profile Picture"
                    className="cursor-pointer rounded w-[36px] h-[36px] ml-2"
                    width={640}
                    height={640}
                    priority
                />
                <ChevronUpIcon className={`${showAccountMenu && "rotate-180"} w-7 h-7 duration-300`}/>
            </div>
            {showAccountMenu && (
                <div className={"absolute z-100 bg-transparent h-full w-[65px] h-full"}
                     onMouseLeave={() => setShowAccountMenu(false)}>
                    <div
                        className="relative md:right-[9.5em] top-[5em] rounded-md shadow-md transform bg-black/70 border w-[65px] md:w-[200px]">
                        <ul className="flex flex-col w-full">
                            <Link href="/account"
                                  className={"text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                <UserIcon className={"h-6 w-6"}/>
                                <p className={"font-poppins p-4 hover:underline hidden md:block"}>Account</p>
                            </Link>
                            <Link href="/settings"
                                  className={"text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                <CogIcon className={"h-6 w-6"}/>
                                <p className={"font-poppins p-4 hover:underline hidden md:block"}>Settings</p>
                            </Link>
                            <Link href="/changelog"
                                  className={"text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                <QuestionMarkCircleIcon className={"h-6 w-6"}/>
                                <p className={"font-poppins p-4 hover:underline hidden md:block"}>Changelog</p>
                            </Link>
                            <Link href={DONATION_URL}
                                  className={"text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                                <Image src={CoffeeIcon} alt={"Coffee Icon"} width={64} height={64} className={"h-6 w-6 text-white"}
                                       priority/>
                                <p className={"font-poppins p-4 hover:underline hidden md:block"}>Donate Coffee</p>
                            </Link>
                        </ul>
                        <div className={"w-full h-[1px] bg-white"}/>
                        <div className={"flex w-full hover:bg-red-900 rounded"}>
                            <button className={"text-white w-full flex flex-row justify-center md:justify-start items-center py-2 px-4"}
                                    onClick={() => UserService.signOut()}>
                                <ArrowLeftOnRectangleIcon className={"h-6 w-6"}/>
                                <p className={"font-poppins p-4 hover:underline hidden md:block"}>Log Out</p>
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
