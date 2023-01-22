import {resolutionState, sourceState} from "@atoms/VideoPlayerAtom";
import {AdjustmentsHorizontalIcon} from "@heroicons/react/24/outline";
import {MediaSource} from "@interfaces/MediaSource";
import React, {useState} from 'react';
import {useRecoilState, useSetRecoilState} from "recoil";

interface Props {
    className?: string;
    mediaSources: MediaSource[];
}

export default function ResolutionMenu({className, mediaSources}: Props) {
    const [showList, setShowList] = useState(false);
    const [resolution, setResolution] = useRecoilState(resolutionState);
    const setSource = useSetRecoilState(sourceState);
    
    function toggleResolution(quality: string) {
        const selectedResolution = mediaSources.find(source => source.quality === quality)?.url
        console.log("[ResolutionMenu] Setting source to '%s' with quality '%s'", selectedResolution, quality);
        if (selectedResolution) {
            setResolution(quality);
            setSource(selectedResolution);
        }
        setShowList(false);
    }
    
    return (
        <div className={`${className ? className : ""} relative items-center flex justify-center`}>
            <button className="videoPlayerControls" onClick={() => setShowList(!showList)}>
                <AdjustmentsHorizontalIcon className={"h-full w-full"}/>
            </button>
            {showList && (
                <ul className="absolute bottom-12 z-10 rounded-md shadow-md transform origin-top-right bg-[#262626]"
                    onMouseLeave={() => setShowList(false)}>
                    {mediaSources.map((mediaSource: MediaSource, index) => (
                        <li key={index}
                            className={`px-4 py-2 rounded-md
                            ${mediaSource.quality === resolution ? "bg-[#131313]" : "hover:bg-[#3c3c3c]"}
                            ${mediaSource.quality === "backup" ? "hidden" : ""}`}
                            onClick={() => toggleResolution(mediaSource.quality)}>
                            <a className="block text-sm text-white font-poppins">{mediaSource.quality}</a>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}
