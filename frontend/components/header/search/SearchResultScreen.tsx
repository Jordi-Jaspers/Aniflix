import {searchResultsState} from "@atoms/SearchResultScreen";
import React from "react";
import {useRecoilValue} from "recoil";

export default function SearchResultScreen() {
    const results = useRecoilValue(searchResultsState)
    return (
        <section className={"min-h-screen h-fit z-10 bg-[#181818]"}>
            <ul className={"text-white"}>
                {results.map((result) => (
                    <li key={result.id}>{result.title.english}</li>
                ))}
            </ul>
        </section>
    )
}
