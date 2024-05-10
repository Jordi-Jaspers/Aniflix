// a map of topics and assigned colors for that topic
export const NEWS_TOPICS: Record<string, string> = {
	'live-action': '#FF6B6B',
	events: '#FFD166',
	games: '#06D6A0',
	anime: '#DC2626',
	people: '#073B4C',
	novels: '#EF476F',
	manga: '#FFD166',
	industry: '#118AB2',
	music: '#073B4C',
	general: '#EF476F'
};

// a map of topics and assigned colors for that topic
export function getNewsTopicColor(topic: string): string {
	return NEWS_TOPICS[topic] ?? '#118AB2';
}
