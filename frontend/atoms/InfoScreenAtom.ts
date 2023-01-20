import {atom} from 'recoil'

export const infoScreenState = atom<boolean>({
    key: 'infoScreenState',
    default: false,
})
