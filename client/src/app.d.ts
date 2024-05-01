// See https://kit.svelte.dev/docs/types#app
// for information about these classs
declare global {
	namespace App {
		// class Error {}
		// class Locals {}
		// class PageData {}
		// class PageState {}
		// class Platform {}
	}
}
// ======================== MODELS ===========================
class CarouselColumns {
	columns: number;
	width: number;
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

class AnimeRequest {
	page: number;
	perPage: number;
	title: string;
	genre: string;
	season: string;
}

// ======================== RESPONSE ========================

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
}

class EpisodeResponse {
	anilistId: number;
	title: string;
	episodeTitle: string;
	episodeNumber: number;
	episodeUrl: string;
	description: string;
	image: string;
	airDate: Date;
	duration: number;
}

class ConstantResponse {
	genres: string[];
	seasons: string[];
	status: string[];
	mediaTypes: string[];
	watchStatus: string[];
}

class InteractionResponse {
	anime: AnimeResponse;
	liked: boolean;
	inLibrary: boolean;
	watchStatus: string;
	lastSeenEpisode: number;
	lastInteraction: Date;
}

// ======================== JWT ========================

class JwtPayload {
	authorities: string[];
	exp: number;
	iat: number;
	iss: string;
	sub: string;
}
