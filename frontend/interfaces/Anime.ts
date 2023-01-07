import {Title} from "./Title";
import {Trailer} from "./Trailer";
import {Date} from "./Date";

export interface Anime {
    id: number,
    title: Title
    startDate: Date,
    releaseDate: number,
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
    studios: string[],
    countryOfOrigin: string,
    recommendations: number[],
}
