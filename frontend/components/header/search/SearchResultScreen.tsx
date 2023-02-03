import {searchResultsState} from "@atoms/SearchResultScreen";
import SearchResult from "@components/header/search/SearchResult";
import React, {useEffect, useState} from "react";
import {useRecoilValue} from "recoil";

export default function SearchResultScreen() {
    const results = useRecoilValue(searchResultsState)
    const [cols, setCols] = useState(1);
    
    useEffect(() => {
        const handleResize = () => {
            const cardWidth = 275;
            const gridWidth = window.innerWidth;
            setCols(Math.floor(gridWidth / cardWidth));
        };
        console.log("useEffect:" + cols)
        handleResize();
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);
    
    return (
        <section className={"min-h-screen h-fit z-10 bg-[#1E1E25] flex justify-evenly"}>
            <div className={`grid gap-4 py-4`}
                 style={{gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`}}>
                {results.map((result) => (
                    <SearchResult anime={result}/>
                ))}
            </div>
        </section>
    )
}
