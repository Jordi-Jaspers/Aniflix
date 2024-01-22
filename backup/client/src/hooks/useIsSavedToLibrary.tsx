"use client"

import {useUserInformation} from "@hooks/useUserInformation";
import LibraryService from "@service/LibraryService";

import {useQuery} from "react-query";

export default function useIsSavedToLibrary(animeId: string) {
    const {id} = useUserInformation();

    const isSavedInLibrary = async () => {
        const record = await LibraryService.getAnimeFromLibrary(animeId);
        return !!record;
    }

    return useQuery(["isSavedToLibrary", animeId, id], isSavedInLibrary)
}

