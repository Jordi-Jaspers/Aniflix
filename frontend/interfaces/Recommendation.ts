import {Title} from "./Title";

export interface Recommendation {
    interfaceType: "Recommendation";
    id: number,
    title: Title
    episodes: number,
    image: string,
    cover: string,
    rating: number,
    type: string,
}
