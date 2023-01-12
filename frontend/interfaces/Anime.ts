import {Title} from "./Title";
import {Trailer} from "./Trailer";
import {Date} from "./Date";

export interface Anime {
    interfaceType: "Anime";
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

export function hasAllProperties(object:any): object is Anime {
    return "id" in object
        && "title" in object
        && "startDate" in object
        && "releaseDate" in object
        && "isAdult" in object
        && "image" in object
        && "cover" in object
        && "trailer" in object
        && "popularity" in object
        && "description" in object
        && "status" in object
        && "totalEpisodes" in object
        && "rating" in object
        && "duration" in object
        && "genres" in object
        && "season" in object
        && "subOrDub" in object
        && "type" in object
        && "studios" in object
        && "countryOfOrigin" in object
        && "recommendations" in object;
}
