import {pb} from "@pocketbase/PocketBase";
import React from "react";

export default function Auth() {
    return (
        <div className={"w-screen h-screen bg-black"}>
            <h4 className={"text-7xl text-white"}>{pb.authStore.isValid.toString()}</h4>
        </div>
    )
}


