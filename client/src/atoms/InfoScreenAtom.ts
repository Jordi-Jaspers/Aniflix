import {atom} from 'recoil'

/**
 * Indicates if the info-screen modal should be shown.
 */
export const infoScreenState = atom<boolean>({
    key: 'infoScreenState',
    default: false,
})
