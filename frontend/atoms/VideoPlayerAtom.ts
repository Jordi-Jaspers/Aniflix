import {Anime} from "@interfaces/Anime";
import {Episode} from "@interfaces/Episode";
import {MediaSources} from "@interfaces/MediaSources";
import {atom, DefaultValue, selector} from 'recoil'

export const animeState = atom<Anime | null>({
    key: 'animeState',
    default: null,
})

export const episodeState = atom<Episode | null>({
    key: 'episodeState',
    default: null
})

export const streamingLinksState = atom<MediaSources | null>({
    key: 'streamingLinksState',
    default: null
})

export const volumeState = atom<number>({
    key: 'volumeState',
    default: 0
})

export const currentTimeState = atom<number>({
    key: 'currentTimeState',
    default: 0
})

export const durationState = atom<number>({
    key: 'durationState',
    default: 0
})

export const resolutionState = atom<string>({
    key: 'resolutionState',
    default: 'default'
})

export const videoPlayerState = selector({
    key: 'videoPlayerState',
    get: ({get}) => {
        return {
            anime: get(animeState) as Anime,
            episode: get(episodeState) as Episode,
            streamingLinks: get(streamingLinksState) as MediaSources,
            volume: get(volumeState) as number,
            currentTime: get(currentTimeState) as number,
            duration: get(durationState) as number,
            resolution: get(resolutionState) as string
        }
    },
    set: ({set}, value) => {
        if (value instanceof DefaultValue) {
            set(animeState, value);
            set(episodeState, value)
            set(streamingLinksState, value)
            set(volumeState, value)
            set(currentTimeState, value)
            set(durationState, value)
            set(resolutionState, value)
        } else {
            set(animeState, value.anime as Anime);
            set(episodeState, value.episode as Episode);
            set(streamingLinksState, value.streamingLinks as MediaSources)
            set(volumeState, value.volume as number)
            set(currentTimeState, value.currentTime as number)
            set(durationState, value.duration as number)
            set(resolutionState, value.resolution as string)
        }
    }
})
