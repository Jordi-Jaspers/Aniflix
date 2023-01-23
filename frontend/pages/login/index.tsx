import {animeState, episodeState, streamingLinksState} from "@atoms/VideoPlayerAtom";
import VideoControls from "@components/watch/controls/VideoControls";
import AnimeService from "@consumet/AnimeService";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import React, {useEffect, useState} from "react";
import {useSetRecoilState} from "recoil";

export default function Login() {
    return (
        <div className={"bg-black w-screen h-screen"}>
            TEST
        </div>
    )
}
