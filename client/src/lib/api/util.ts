import { curl } from '$lib/api/client';
import { SERVER_URLS } from '$lib/api/paths';
import { useModalInfo, useShowInfoModal } from '$lib/components/store/localstorage';

export function getRandomValues(array: any[], count: number) {
	for (let i = array.length - 1; i > 0; i--) {
		const j = Math.floor(Math.random() * (i + 1));
		[array[i], array[j]] = [array[j], array[i]];
	}

	return array.slice(0, count);
}

export function calculatePasswordStrength(password: string): number {
	let score = 0;

	const lengthRule = password.length >= 8 && password.length <= 16;
	const upperCaseRule = /[A-Z]/.test(password);
	const lowerCaseRule = /[a-z]/.test(password);
	const digitRule = /[0-9]/.test(password);
	const specialCharRule = /[^A-Za-z0-9]/.test(password);
	const whitespaceRule = !/\s/.test(password);

	if (lengthRule) score += 1;
	if (upperCaseRule) score += 1;
	if (lowerCaseRule) score += 1;
	if (digitRule) score += 1;
	if (specialCharRule) score += 1;
	if (whitespaceRule) score += 1;

	return score;
}

export function getPasswordStrengthLabel(score: number): string {
	switch (score) {
		case 6:
			return 'Very Strong';
		case 5:
			return 'Strong';
		case 4:
			return 'Medium';
		case 3:
			return 'Weak';
		default:
			return 'Very Weak';
	}
}

export function isIOS(userAgent: string): boolean {
	const platform = navigator?.platform || '';
	return /iPad|iPhone|iPod/.test(userAgent) || (platform === 'MacIntel' && navigator.maxTouchPoints > 1);
}

export async function openModal(anilistId: number): Promise<void> {
	const response: Response = await curl(SERVER_URLS.ANIME_INFO_PATH.replace('{id}', anilistId.toString()), {
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
