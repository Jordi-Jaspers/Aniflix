"use client"

import type {Anime} from "@interfaces/Anime";

import {useEffect, useState} from "react";

export const useFetchRandomAnime = (request: (() => Promise<Anime[]>)) => {
    const [anime, setAnime] = useState<Anime>()
    const [title, setTitle] = useState<string>("")
    const [description, setDescription] = useState<string>("")
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        request().then((response: Anime[]) => {
            if (response !== undefined && response.length !== undefined) {
                const anime: Anime = response[Math.floor(Math.random() * response.length)] as Anime
                setAnime(anime)
                setTitle(anime.title.romaji ? anime.title.romaji : anime.title.english)
                setDescription(anime.description.length > 500 ? anime.description.substring(0, 500) + '...' : anime.description)
                setLoading(false)
            }
        })
    }, [request])

    return {anime, title, description, loading}
};
