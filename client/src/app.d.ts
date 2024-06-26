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
class UpdateEpisodeProgressRequest {
	anilistId: number;
	episode: number;
	lastSeen: number;
}

class PasswordRequest {
	newPassword: string;
	confirmPassword: string;
}

class UpdatePasswordRequest extends PasswordRequest {
	oldPassword: string;
}

class ForgotPasswordRequest extends PasswordRequest {
	token: string;
}

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

class UpdateUserDetailsRequest {
	firstName: string;
	lastName: string;
}

class UpdateEmailRequest {
	email: string;
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
	email: string;
	firstName: string;
	lastName: string;
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
	clearLogo: string;
	fanArt: string;
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
	summary: string;
	image: string;
	airDate: Date;
	duration: number;
	streamingLinks: StreamingLinksResponse;
	lastSeen: number;
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
