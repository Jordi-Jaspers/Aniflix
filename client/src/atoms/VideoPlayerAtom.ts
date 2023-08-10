import type {Anime} from "@interfaces/Anime";
import type {Episode} from "@interfaces/Episode";
import type {MediaSources} from "@interfaces/MediaSources";

import {atom, DefaultValue, selector} from 'recoil'

/**
 * The Anime metadata of the current playing episode.
 */
export const animeState = atom<Anime | null>({
    key: 'animeState',
    default: null,
})

/**
 * The current episode of the anime with its metadata.
 */
export const episodeState = atom<Episode | null>({
    key: 'episodeState',
    default: null
})

/**
 * All the media sources for the current episode
 */
export const streamingLinksState = atom<MediaSources | null>({
    key: 'streamingLinksState',
    default: null
})

/**
 * The volume of the video player in the range of 0 to 100.
 */
export const volumeState = atom<number>({
    key: 'volumeState',
    default: 50
})

/**
 * Indicates if the video player is muted or not.
 */
export const isMuteState = atom<boolean>({
    key: 'isMuteState',
    default: false
})

/**
 * The current time of the video player in seconds.
 */
export const currentTimeState = atom<number>({
    key: 'currentTimeState',
    default: 0
})

/**
 * The duration of the video player in seconds.
 */
export const durationState = atom<number>({
    key: 'durationState',
    default: 0
})

export const isPlayingState = atom<boolean>({
    key: 'isPlayingState',
    default: true
})

/**
 * The resolution of the episode that is currently playing.
 */
export const resolutionState = atom<string>({
    key: 'resolutionState',
    default: '1080p'
})

/**
 * The url of the resolution that is currently playing.
 */
export const sourceState = atom<string>({
    key: 'sourceState',
    default: 'https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8',
})

/**
 * The selector that returns the current playing episode.
 */
export const videoPlayerState = selector({
    key: 'videoPlayerState',
    get: ({get}) => {
        return {
            anime: get(animeState) as Anime,
            episode: get(episodeState) as Episode,
            streamingLinks: get(streamingLinksState) as MediaSources,
            volume: get(volumeState) ,
            isMuted: get(isMuteState) ,
            currentTime: get(currentTimeState) ,
            duration: get(durationState) ,
            isPlaying: get(isPlayingState) ,
            resolution: get(resolutionState) ,
            source: get(sourceState) 
        }
    },
    set: ({set}, value) => {
        if (value instanceof DefaultValue) {
            set(animeState, value);
            set(episodeState, value)
            set(streamingLinksState, value)
            set(volumeState, value)
            set(isMuteState, value)
            set(currentTimeState, value)
            set(durationState, value)
            set(isPlayingState, value)
            set(resolutionState, value)
            set(sourceState, value)
        } else {
            set(animeState, value.anime );
            set(episodeState, value.episode );
            set(streamingLinksState, value.streamingLinks )
            set(volumeState, value.volume )
            set(isMuteState, value.isMuted )
            set(currentTimeState, value.currentTime )
            set(durationState, value.duration )
            set(isPlayingState, value.isPlaying )
            set(resolutionState, value.resolution )
            set(sourceState, value.source )
        }
    }
})
