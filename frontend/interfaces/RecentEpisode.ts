import {Genres} from "@enum/Genre";
import {MediaType} from "@enum/MediaType";
import {Title} from "@interfaces/Title";

export interface RecentEpisode {
    interfaceType: "RecentEpisode";
    id: number,
    malId: number,
    title: Title,
    image: string,
    rating: number,
    color: string,
    episodeId: string,
    episodeTitle: string,
    episodeNumber: number,
    genres: Genres[],
    type: MediaType
}
