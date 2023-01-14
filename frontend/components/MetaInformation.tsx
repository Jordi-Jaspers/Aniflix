import {Anime} from "@interfaces/Anime";
import React from "react";

interface Props {
    className?: string;
    anime: Anime
}

export default function MetaInformation({className, anime}: Props) {
    return (
        <>
            {anime && (
                <div className={className ? className : ""}>
                    <h4 className={"infoScreenTitle"}>About {anime.title.romaji ? anime.title.romaji : anime.title.english}</h4>

                    {/* Characters: */}
                    <div className={"metaInfoTitle"}>
                        <span className="text-[gray]">Characters:</span>{' '}
                        {anime.characters.map((character) => character.name.full).join(', ')}
                    </div>

                    {/* Country of Origin: */}
                    <div className={"metaInfoTitle"}>
                        <span className="text-[gray]">Country Of Origin:</span>{' '}
                        {anime?.countryOfOrigin}
                    </div>

                    {/* Studios: */}
                    <div className={"metaInfoTitle"}>
                        <span className="text-[gray]">Studios:</span>{' '}
                        {anime?.studios.map((studio) => studio).join(', ')}
                    </div>

                    {/* Genres: */}
                    <div className={"metaInfoTitle"}>
                        <span className="text-[gray]">Genres:</span>{' '}
                        {anime.genres.map((genre) => genre).join(', ')}
                    </div>

                    {/* Age Category: */}
                    <div className={"metaInfoTitle"}>
                        <span className="text-[gray]">Age Category:</span>{' '}
                        {anime.isAdult ? "PG-18" : "PG-16"}
                    </div>
                </div>
            )}
        </>
    )
}
