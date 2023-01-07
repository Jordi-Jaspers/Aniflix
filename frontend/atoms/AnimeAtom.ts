import {atom} from 'recoil'
import {GoEpisode} from "../interfaces/GoEpisode";
import {Anime} from "../interfaces/Anime";

export const animeState = atom<Anime | null>({
    key: 'animeState',
    default: null,
})

export const infoScreenState = atom({
    key: 'infoScreenState',
    default: false,
})
