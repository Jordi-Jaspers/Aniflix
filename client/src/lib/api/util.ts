import {curl} from "$lib/api/client";
import {useAnimeInfo, useShowInfoModal} from "$lib/store";
import {SERVER_URLS} from "$lib/api/paths";

export function getRandomValues(array: any[], count: number) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }

    return array.slice(0, count)
}

export async function openModal(anilistId: number): Promise<void> {
    const response: Response = await curl(
        SERVER_URLS.ANIME_DETAILS_PATH.replace('{id}', anilistId.toString()),
        {
            method: 'GET'
        });

    if (response.ok) {
        const interaction: InteractionResponse = await response.json();
        useAnimeInfo.set(interaction.anime);
        useShowInfoModal.set(true);
    }
}

