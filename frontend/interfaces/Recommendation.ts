import {MediaType} from "@enum/MediaType";
import {Status} from "@enum/Status";
import {Title} from "./Title";

export interface Recommendation {
    interfaceType: "Recommendation",
    id: string,
    title: Title,
    status: Status,
    episodes: number,
    image: string,
    cover: string,
    rating: number,
    type: MediaType
}
