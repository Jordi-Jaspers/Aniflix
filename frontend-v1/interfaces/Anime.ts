import {Title} from "./Title";
import {Trailer} from "./Trailer";

export interface Anime {
    id: number,
    title: Title
    isAdult: boolean,
    image: string,
    cover: string,
    trailer: Trailer
    popularity: number,
    description: string,
    status: string,
    totalEpisodes: number,
    rating: number,
    duration: number,
    genres: string[],
    season: string,
    subOrDub: string,
    type: string,
    recommendations: number[],
}
