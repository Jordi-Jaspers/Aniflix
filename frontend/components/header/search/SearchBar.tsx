"use client"
import {searchResultsState, showSearchResultsState} from "@atoms/SearchResultScreen";
import AnimeService from "@consumet/AnimeService";

import React, {useEffect, useRef, useState} from "react";

import {MagnifyingGlassIcon} from "@heroicons/react/24/solid";
import {useSetRecoilState} from "recoil";

const SearchBar = () => {
    const [isExpanded, setExpanded] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const inputElement = useRef<HTMLInputElement>(null);
    const setShowSearchResults = useSetRecoilState(showSearchResultsState);
    const setSearchResults = useSetRecoilState(searchResultsState);
    
    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        console.debug(e.key)
        if (e.key === "Escape") {
            setSearchTerm("");
            setExpanded(false);
        }
    }
    
    const handleFocus = () => {
        if (!inputElement.current) return;
        setExpanded(!isExpanded)
        inputElement.current.focus();
    }
    
    useEffect(() => {
        if (!isExpanded) {
            setSearchTerm("");
        }
    }, [isExpanded]);
    
    useEffect(() => {
        if (searchTerm.length > 0) {
            setShowSearchResults(true);
            const params: Map<string, string> = new Map<string, string>();
            params.set("sort", `["SCORE_DESC", "POPULARITY_DESC"]`)
            AnimeService.searchAnime(searchTerm, params).then((res) => {
                setSearchResults(res);
            });
        } else {
            setSearchResults([]);
            setShowSearchResults(false);
        }
    }, [searchTerm]);
    
    return (
        <div className="relative flex flex-row items-center">
            <input
                className={`${
                    isExpanded
                        ? "ease-out duration-500 w-36 sm:w-48 mt-1 py-0.5 px-2 sm:py-2 rounded border border-gray-400 transition-all"
                        : "w-0 ease-in duration-500"
                } bg-[#181818]/80 font-poppins text-sm mr-1 sm:mr-4`}
                onChange={(e) => setSearchTerm(e.target.value)}
                value={searchTerm}
                type="text"
                placeholder="Search"
                onKeyDown={(e) => handleKeyDown(e)}
                ref={inputElement}
            />
            <MagnifyingGlassIcon className="headerIcon" onClick={() => handleFocus()}/>
        </div>
    );
};

export default SearchBar;
