"use client"

import type {Anime} from "@interfaces/Anime";
import type {RecentEpisode} from "@interfaces/RecentEpisode";

import {useEffect, useState} from "react";

export const useFetchAnime = (request: ((genre?: string) => Promise<Anime[] | RecentEpisode[]>), genre?: string) => {
    const [animes, setAnimes] = useState<Anime[] | RecentEpisode[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        async function fetchAnime() {
            if (request) {
                const response = genre ? await request(genre) : await request()
                if (response.length !== undefined) {
                    setAnimes(response)
                }
            }
        }

        fetchAnime()
            .then(() => setLoading(false))
            .catch(() => setLoading(true))
    }, [request, genre])

    return {animes, loading};
};
