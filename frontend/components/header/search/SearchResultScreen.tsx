import {searchResultsState} from "@atoms/SearchResultScreen";
import ResultCard from "@components/ResultCard";
import {useDynamicColumns} from "@hooks/useDynamicColumns";
import React from "react";
import {useRecoilValue} from "recoil";

export default function SearchResultScreen() {
    const results = useRecoilValue(searchResultsState)
    const cols = useDynamicColumns(275)
    return (
        <section className={"min-h-screen h-fit z-10 bg-[#1E1E25] flex justify-evenly"}>
            <div className={`grid gap-4 py-4 h-fit`}
                 style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                {results.map((result) => (
                    <ResultCard anime={result}/>
                ))}
            </div>
        </section>
    )
}
