import {type Writable, writable} from 'svelte/store';

export const useAuthenticated:Writable<boolean> = writable(false);
export const useErrorMessage:Writable<string> = writable('');
export const useHasError:Writable<boolean> = writable(false);
export const useIsLoading:Writable<boolean> = writable(false);

export const useShowInfoModal:Writable<boolean> = writable(false);

export const useAnimeInfo:Writable<AnimeResponse> = writable({
    id: 0,
    title: '',
    description: '',
    totalEpisodes: 0,
    rating: 0,
    status: '',
    image: '',
    cover: '',
    trailer: '',
    releaseYear: 0,
    genres: [],
    mediaType: '',
    subbed: false,
    likes: 0
});
