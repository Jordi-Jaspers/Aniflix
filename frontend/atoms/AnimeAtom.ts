import {atom} from 'recoil'
import {Anime} from "../interfaces/Anime";

export const animeState = atom<Anime | null>({
    key: 'animeState',
    default: null,
})

export const infoScreenState = atom<boolean>({
    key: 'infoScreenState',
    default: false,
})
