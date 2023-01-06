import {atom} from 'recoil'
import {GoEpisode} from "../interfaces/GoEpisode";
import {Anime} from "../interfaces/Anime";

export const animeState = atom<Anime | GoEpisode | null>({
    key: 'animeState',
    default: null,
})
