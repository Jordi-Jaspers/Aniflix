import React from "react";

import {PlayIcon} from "@heroicons/react/24/solid";

interface Props {
    buttonClassName?: string;
    iconClassName?: string;
}

export default function HoveringPlayIcon({buttonClassName, iconClassName}: Props) {
    return (
        <div
            className={"align-center flex h-full justify-center items-center absolute top-0 bottom-0 left-0 right-0 opacity-0 hover:transition-opacity hover:opacity-100 hover:duration-200 hover:ease-in"}>
            <button
                className={`${buttonClassName ? buttonClassName : ""} bg-[#141414]/60 flex h-14 w-14 items-center justify-center rounded-full border-[1px] border-white`}>
                <PlayIcon className={`${iconClassName ? iconClassName : ""} text-white h-10 w-10`}/>
            </button>
        </div>
    )
}
