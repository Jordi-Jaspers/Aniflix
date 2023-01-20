import {resolutionState} from "@atoms/VideoPlayerAtom";
import {AdjustmentsHorizontalIcon} from "@heroicons/react/24/outline";
import {MediaSource} from "@interfaces/MediaSource";
import React, {useState} from 'react';
import {useRecoilState} from "recoil";

interface Props {
    className?: string;
    mediaSources: MediaSource[];
}

export default function ResolutionMenu({className, mediaSources}: Props) {
    const [showList, setShowList] = useState(false);
    const [resolution, setResolution] = useRecoilState(resolutionState);
    
    function toggleResolution(quality: string) {
        const selectedResolution = mediaSources.find(source => source.quality === quality)?.url
        if (selectedResolution) setResolution(selectedResolution);
    }
    
    return (
        <div className={`${className ? className : ""} relative items-center flex justify-center`}>
            <button className="videoPlayerControls" onClick={() => setShowList(!showList)}>
                <AdjustmentsHorizontalIcon className={"h-full w-full"}/>
            </button>
            {showList && (
                <ul className="absolute bottom-12 z-10 rounded-md shadow-md transform origin-top-right bg-[#141414]">
                    {mediaSources.map((mediaSource: MediaSource, index) => (
                        <li key={index} className={`${mediaSource.url === resolution ? "bg-[#f00]" : ""} px-4 py-2 rounded-md hover:bg-[#f00]`} onClick={() => toggleResolution(mediaSource.quality)}>
                            <a className="block text-sm text-white font-poppins">{mediaSource.quality}</a>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}
