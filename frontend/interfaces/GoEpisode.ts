export interface GoEpisode {
    id: string,
    episodeId: string,
    episodeNumber: number,
    title: string,
    image: string
}

export function isGoEpisode(obj: any): boolean {
    return obj && obj.id && obj.episodeId && obj.episodeNumber && obj.title && obj.image;
}
