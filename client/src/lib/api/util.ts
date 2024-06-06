export function getRandomValues(array: any[], count: number) {
	for (let i = array.length - 1; i > 0; i--) {
		const j = Math.floor(Math.random() * (i + 1));
		[array[i], array[j]] = [array[j], array[i]];
	}

	return array.slice(0, count);
}

export function calculatePasswordStrength(password: string): { score: number; missing: string[] } {
	let score = 0;
	let missing = [];

	const lengthRule = password.length >= 8;
	const tooLongRule = password.length <= 100;
	const upperCaseRule = /[A-Z]/.test(password);
	const lowerCaseRule = /[a-z]/.test(password);
	const digitRule = /[0-9]/.test(password);
	const specialCharRule = /[^A-Za-z0-9]/.test(password);
	const whitespaceRule = !/\s/.test(password);

	if (lengthRule) score += 1;
	else missing.push('At least 8 characters');
	if (tooLongRule) score += 1;
	else missing.push('At most 100 characters');
	if (upperCaseRule) score += 1;
	else missing.push('At least one upper-case character');
	if (lowerCaseRule) score += 1;
	else missing.push('At least one lower-case character');
	if (digitRule) score += 1;
	else missing.push('At least one digit character');
	if (specialCharRule) score += 1;
	else missing.push('At least one special character');
	if (whitespaceRule) score += 1;
	else missing.push('No whitespace allowed');

	return { score, missing };
}

export function getPasswordStrengthLabel(score: number): string {
	switch (score) {
		case 7:
			return 'Very Strong';
		case 6:
			return 'Strong';
		case 5:
			return 'Medium';
		case 4:
			return 'Weak';
		default:
			return 'Very Weak';
	}
}

export function isIOS(userAgent: string): boolean {
	const platform = navigator?.platform || '';
	return /iPad|iPhone|iPod/.test(userAgent) || (platform === 'MacIntel' && navigator.maxTouchPoints > 1);
}
