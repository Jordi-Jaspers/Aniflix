import {Episode} from "../interfaces/Episode";
import Image from "next/image";
import {Recommendation} from "../interfaces/Recommendation";
import {Anime} from "../interfaces/Anime";
import ConsumetApi from "../utils/ConsumetApi";

interface Props {
    anime: Recommendation
}

export default function RecommendationCard({anime}: Props) {

    const title: string = anime.title.romaji ? anime.title.romaji : anime.title.english;

    console.log(anime)
    return (
        <div className={"m-[0.1em] min-h-[22em] h-[100%] cursor-pointer rounded bg-[#2f2f2f]"}>
            <Image
                src={anime.image}
                alt={title}
                className={"rounded-t aspect-video object-cover w-full"}
                width={150}
                height={150}
            />
            <div className={"min-h-[100%]"}>
                <div>

                </div>
                <p>
                    {title}
                </p>
            </div>
        </div>
    )
}


