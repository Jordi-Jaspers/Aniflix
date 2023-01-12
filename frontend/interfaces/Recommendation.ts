import {Title} from "./Title";

export interface Recommendation {
    interfaceType: "Recommendation",
    id: string,
    title: Title,
    status: string,
    episodes: number,
    image: string,
    cover: string,
    rating: number,
    type: string
}
