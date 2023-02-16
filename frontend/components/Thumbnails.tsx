import Thumbnail from "@components/Thumbnail";
import {useFetchAnime} from "@hooks/useFetchAnime";
import {Anime} from "@interfaces/Anime";
import {RecentEpisode} from '@interfaces/RecentEpisode';

interface Props {
    request: () => Promise<Anime[]> | Promise<RecentEpisode[]>
    genre?: string
    priority?: boolean
}

export default function Thumbnails({request, genre, priority}: Props) {
    const {animes, loading} = useFetchAnime(request, genre)
    return (
        <>
            {animes?.map((anime, index) => {
                if (index < 25) return (<Thumbnail key={anime.id} anime={anime} loading={loading} priority={priority}/>)
            })}
        </>
    )
}
