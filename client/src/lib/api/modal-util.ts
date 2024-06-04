import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { useModalInfo, useShowInfoModal } from '$lib/components/store/localstorage';
import { toast } from 'svelte-sonner';

let isRequesting = false;
export async function openModal(anilistId: number): Promise<void> {
	if (isRequesting) return;
	toast.promise(getAnimeInfo(anilistId), {
		loading: 'Fetching anime info...'
	});
}

export function closeModal(): void {
	useModalInfo.set({} as AnimeResponse);
	useShowInfoModal.set(false);
}

async function getAnimeInfo(anilistId: number): Promise<void> {
	isRequesting = true;
	try {
		const response: Response = await curl(SERVER_URLS.ANIME_INFO_PATH.replace('{id}', anilistId.toString()), {
			method: 'GET'
		});

		if (response.ok) {
			const anime: AnimeResponse = await response.json();
			useModalInfo.set(anime);
			useShowInfoModal.set(true);
		} else {
			closeModal();
		}
	} finally {
		isRequesting = false;
	}
}
