import {ArrowLeftOnRectangleIcon, CogIcon, QuestionMarkCircleIcon, UserIcon} from "@heroicons/react/24/outline";
import {ChevronUpIcon} from "@heroicons/react/24/solid";
import {useUserInformation} from "@hooks/useUserInformation";
import UserService from "@service/UserService";
import CoffeeIcon from "@svg/Coffee.png";
import Image from "next/image";
import Link from "next/link";
import React, {useState} from 'react'

export default function AccountMenu() {
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const user = useUserInformation();
    return (
        <div className={"flex flex-row w-[60px] h-[32px] items-center"}
             onMouseEnter={() => setShowAccountMenu(true)}>
            <ChevronUpIcon className={`${showAccountMenu && "rotate-180"} w-4 h-4 duration-300`}/>
            <Image
                src={UserService.getFileUrl(user, user?.avatar)}
                alt="Profile Picture"
                className="cursor-pointer rounded w-[32px] h-[32px] ml-2"
                width={2000}
                height={2000}
            />
            {showAccountMenu && (
                <div
                    className="absolute z-100 top-[5em] right-4 md:right-6 lg:right-12 rounded-md shadow-md transform bg-black/70 border w-[60px] md:w-[200px]"
                    onMouseLeave={() => setShowAccountMenu(false)}>
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
                        <Link href="/donate"
                              className={"text-white flex flex-row justify-center md:justify-start items-center py-2 px-4 hover:bg-gray-600 rounded"}>
                            <Image src={CoffeeIcon} alt={"Coffee Icon"} width={24} height={24} className={"h-6 w-6 text-white"}/>
                            <p className={"font-poppins p-4 hover:underline hidden md:block"}>Buy Me A Coffee</p>
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
            )}
        </div>
    )
}
