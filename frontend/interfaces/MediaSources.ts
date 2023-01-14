import {MediaSource} from "@interfaces/MediaSource";

export interface MediaSources {
    interfaceType: "MediaSources";
    headers: {
        referer: string,
    },
    sources: MediaSource[]
}
