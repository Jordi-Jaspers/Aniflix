import {Anime} from "@interfaces/Anime";
import {atom, DefaultValue, selector} from 'recoil'

/**
 * Indicates if the search result screen is visible.
 */
export const showSearchResultsState = atom<boolean>({
    key: 'showSearchResultsState',
    default: false,
})

export const searchResultsState = atom<Anime[]>({
    key: 'searchResultsState',
    default: [],
})

export const searchResultsSelectorState = selector({
    key: 'searchResultsSelectorState',
    get: ({get}) => {
        return {
            searchResults: get(searchResultsState),
            showSearchResults: get(showSearchResultsState)
        }
    },
    set: ({set}, newValue) => {
        if (newValue instanceof DefaultValue) {
            set(searchResultsState, [])
            set(showSearchResultsState, false)
        } else {
            set(searchResultsState, newValue.searchResults as Anime[])
            set(showSearchResultsState, newValue.showSearchResults as boolean)
        }
    }
})
