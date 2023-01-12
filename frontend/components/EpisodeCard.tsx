import {Episode} from "../interfaces/Episode";
import Image from "next/image";

interface Props {
    episode: Episode
}

export default function EpisodeCard({episode}: Props) {
    let date = new Date(episode.airDate);
    let airDate = date.toLocaleDateString();
    return (
        <div className={"flex justify-center"}>
            <div className={"border-y-[1px] border-[#404040] rounded w-full flex px-6 py-8 cursor-pointer hover:bg-[#333]"}>
                <div className={"text-[#d2d2d2] text-2xl h-fit font-poppins text-center self-center w-fit w-8 pr-4"}>
                    {episode?.number}
                </div>
                <Image
                    src={episode.image}
                    alt={episode.title}
                    className={"rounded-xl aspect-video object-fill self-center"}
                    width={150}
                    height={150}
                />
                <div>
                    <div className={"pl-6"}>
                        <div className={"flex flex-row justify-between pb-2"}>
                            <div className={"text-white font-bold font-poppins"}>
                                {episode.title}
                            </div>
                            <div className={"text-white font-bold font-poppins"}>
                                {airDate}
                            </div>
                        </div>
                        <p className={"text-[#d2d2d2] font-poppins font-light text-sm"}>
                            {episode.description}
                        </p>
                    </div>
                    <p></p>
                </div>
            </div>
        </div>
    )
}
