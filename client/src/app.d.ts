declare global {
	// declare const __NAME__: string;
	// declare const __VERSION__: string;
	namespace App {}
}

// ======================== MODELS ===========================
class AnimeInfo {
	anilistId: number;
	inLibrary: boolean;
	liked: boolean;
}

class SveltePlayerConfig {
	url: string = '';
	pip: boolean = false;
	playing: boolean = true;
	controls: boolean = false;
	light: boolean = false;
	volume: number = 1.0;
	muted: boolean = false;
	played: number = 0;
	loaded: number = 0;
	duration: number = 0;
	playbackRate: number = 1.0;
	loop: boolean = false;
	urlInput: string = '';
	seeking: boolean = false;
}

// ======================== EXCEPTION ========================
class Exception {
	method: string;
	uri: string;
	query: null;
	contentType: string;
	statusCode: number;
	statusMessage: string;
	errorMessage: string;
	apiErrorCode: string;
	apiErrorReason: string;
}

class ValidationException {
	method: string;
	uri: string;
	query: null;
	contentType: string;
	statusCode: number;
	statusMessage: string;
	errorMessage: string;
	errors: ValidationField[];
}

class ValidationField {
	code: string;
	field: string;
}

// ======================== REQUESTS ========================

class LoginRequest {
	email: string;
	password: string;
}

class PageRequest {
	page: number;
	perPage: number;
}

class RefreshTokenRequest {
	refreshToken: string;
}

class RegisterRequest {
	email: string;
	firstName: string;
	lastName: string;
	password: string;
	passwordConfirmation: string;
}

class AnimeRequest extends PageRequest {
	title: string;
	genre: string | null;
	season: string | null;
}

class LibrarySearchRequest {
	page: number;
	pageSize: number;
	query: string;
	watchStatus: string[];
	genre: string[];
	status: string[];
	minRating: number;
	maxRating: number;
	afterYear: number;
	beforeYear: number;
}

// ======================== RESPONSE ========================

class PageResponse<T> {
	content: T[];
	totalElements: number;
	totalPages: number;
	pageNumber: number;
	pageSize: number;
}

class NextAiringEpisodeResponse {
	hasNextEpisode: boolean;
	airingTime: Date;
	timeUntilAiring: number;
	episode: number;
}

class RegisterResponse {
	email: string;
	authorities: string[];
	enabled: boolean;
	validated: boolean;
}

class UserDetailsResponse {
	firstName: string;
	lastName: string;
	email: string;
	authorities: string[];
	lastLogin: Date;
	enabled: boolean;
	validated: boolean;
}

class AuthorizeResponse {
	email: string;
	authorities: string[];
	lastLogin: Date;
	accessToken: string;
	refreshToken: string;
	expiresAt: Date;
	enabled: boolean;
	validated: boolean;
}

class InteractionResponse {
	anime: AnimeResponse;
	liked: boolean;
	inLibrary: boolean;
	watchStatus: string;
	lastSeenEpisode: number;
	lastInteraction: Date;
}

class RecommendationResponse {
	anilistId: number;
	malId: number;
	title: string;
	status: string;
	episodes: number;
	image: string;
	cover: string;
	rating: number;
	type: string;
	liked: boolean;
	inLibrary: boolean;
	lastSeenEpisode: number;
	watchStatus: string;
	lastInteraction: Date;
}

class AnimeResponse {
	anilistId: number;
	title: string;
	description: string;
	totalEpisodes: number;
	rating: number;
	status: string;
	image: string;
	cover: string;
	trailer: string;
	releaseYear: number;
	genres: string[];
	mediaType: string;
	subbed: boolean;
	likes: number;
	episodes: EpisodeResponse[];
	liked: boolean;
	inLibrary: boolean;
	lastSeenEpisode: number;
	watchStatus: string;
	lastInteraction: Date;
}

class EpisodeResponse {
	anilistId: number;
	title: string;
	episodeTitle: string;
	episodeNumber: number;
	totalEpisodes: number;
	image: string;
	airDate: Date;
	duration: number;
	streamingLinks: StreamingLinksResponse;
}

class StreamingLinksResponse {
	referer: string;
	sources: StreamingSourcesResponse[];
}

class StreamingSourcesResponse {
	src: string;
	quality: string;
}

class NewsPostResponse {
	id: number;
	articleId: string;
	title: string;
	uploadedAt: Date;
	intro: string;
	description: string;
	topic: string;
	thumbnail: string;
	url: string;
}

class ConstantResponse {
	genres: string[];
	seasons: string[];
	status: string[];
	mediaTypes: string[];
	watchStatus: string[];
}

// ======================== JWT ========================

class JwtPayload {
	authorities: string[];
	exp: number;
	iat: number;
	iss: string;
	sub: string;
}
