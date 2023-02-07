import {Anime} from "@interfaces/Anime";
import {RecentEpisode} from "@interfaces/RecentEpisode";
import {useEffect, useState} from "react";

export const useFetchAnime = (request: ((genre?: string) => Promise<Anime[]> | Promise<RecentEpisode[]>), genre?: string) => {
    const [animes, setAnimes] = useState<Anime[] | RecentEpisode[]>([])
    useEffect(() => {
        async function fetchAnime() {
            if (request) {
                const response = genre ? await request(genre) : await request()
                setAnimes(response)
            }
        }
        fetchAnime().then(r => r)
    }, [request, genre])
    
    return animes;
};