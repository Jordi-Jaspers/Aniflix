import {PlusIcon} from "@heroicons/react/24/solid";
import React from "react";

interface Props {
    buttonClassName?: string;
    iconClassName?: string;
}

export default function AddToLibraryIcon({buttonClassName, iconClassName}: Props) {
    return (
        <button className={`${buttonClassName ? buttonClassName : ""} defaultButtonRound`}>
            <PlusIcon className={`${iconClassName ? iconClassName : ""} text-white`}/>
        </button>
    )
};
