import AnimeService from "@consumet/AnimeService";
import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import {useEffect, useState} from "react";

export const useFetchEpisode = (anime_id: string, episode_id: string) => {
    const [anime, setAnime] = useState<Anime>()
    const [episode, setEpisode] = useState<Episode>()
    const [streamingSources, setStreamingSources] = useState<MediaSources>()
    const [loading, setLoading] = useState(true)
    
    useEffect(() => {
        async function fetchEpisode() {
            await AnimeService.getEpisodeLinks(anime_id, parseInt(episode_id)).then(([animeRes, episodeRes, sourcesRes]) => {
                if (!animeRes || !episodeRes || !sourcesRes) return;
                setAnime(animeRes)
                setEpisode(episodeRes)
                setStreamingSources(sourcesRes)
            })
        }
        fetchEpisode().then(() => setLoading(false))
    }, [anime_id, episode_id])
    
    return {anime, episode, streamingSources, loading};
};
