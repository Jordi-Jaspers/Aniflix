import {searchResultsState} from "@atoms/SearchResultScreen";
import ResultCard from "@components/ResultCard";
import {useDynamicColumns} from "@hooks/useDynamicColumns";

import React from "react";

import {useRecoilValue} from "recoil";

export default function SearchResultScreen() {
    const results = useRecoilValue(searchResultsState)
    const {cols, width} = useDynamicColumns()
    return (
        <section className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 flex flex-col"}>
            <div className={"min-h-screen h-fit z-10 flex justify-evenly"}>
                <div className={`grid gap-4 py-4 h-fit`}
                     style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                    {results.map((result) => (
                        <ResultCard
                            key={result.id.toString()}
                            id={result.id.toString()}
                            title={result.title.english ? result.title.english : result.title.romaji}
                            image={result.image}
                            rating={result.rating}
                            status={result.status}
                            totalEpisodes={result.totalEpisodes}
                            width={width}
                        />
                    ))}
                </div>
            </div>
        </section>
    )
}
