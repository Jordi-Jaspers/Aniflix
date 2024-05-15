import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { useModalInfo, useShowInfoModal } from '$lib/components/store/store';

export function getRandomValues(array: any[], count: number) {
	for (let i = array.length - 1; i > 0; i--) {
		const j = Math.floor(Math.random() * (i + 1));
		[array[i], array[j]] = [array[j], array[i]];
	}

	return array.slice(0, count);
}

export async function openModal(anilistId: number): Promise<void> {
	const response: Response = await curl(SERVER_URLS.ANIME_DETAILS_PATH.replace('{id}', anilistId.toString()), {
		method: 'GET'
	});

	if (response.ok) {
		const anime: AnimeResponse = await response.json();
		useModalInfo.set(anime);
		useShowInfoModal.set(true);
	}
}

export function closeModal(): void {
	useModalInfo.set({} as AnimeResponse);
	useShowInfoModal.set(false);
}

export function isIOS(userAgent: string): boolean {
	const platform = navigator?.platform || '';
	return /iPad|iPhone|iPod/.test(userAgent) || (platform === 'MacIntel' && navigator.maxTouchPoints > 1);
}
