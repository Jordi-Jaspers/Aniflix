// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

// ======================== EXCEPTION ========================
interface Exception {
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

interface ValidationException {
	method: string;
	uri: string;
	query: null;
	contentType: string;
	statusCode: number;
	statusMessage: string;
	errorMessage: string;
	errors: ValidationField[];
}

interface ValidationField {
	code: string;
	field: string;
}

// ======================== REQUESTS ========================

interface LoginRequest {
	email: string;
	password: string;
}

interface RefreshTokenRequest {
	refreshToken: string;
}

interface RegisterRequest {
	email: string;
	firstName: string;
	lastName: string;
	password: string;
	passwordConfirmation: string;
}

interface AnimeRequest {
	page: number;
	perPage: number;
	title: string;
	genre: string;
	season: string;
}

// ======================== RESPONSE ========================

interface RegisterResponse {
	email: string;
	authorities: string[];
	enabled: boolean;
	validated: boolean;
}

interface UserDetailsResponse {
	firstName: string;
	lastName: string;
	email: string;
	authorities: string[];
	lastLogin: Date;
	enabled: boolean;
	validated: boolean;
}

interface AuthorizeResponse {
	email: string;
	authorities: string[];
	lastLogin: Date;
	accessToken: string;
	refreshToken: string;
	expiresAt: Date;
	enabled: boolean;
	validated: boolean;
}

interface AnimeResponse {
	id: number;
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
}

interface EpisodeResponse {
	anilistId: string;
	title: string;
	episodeTitle: string;
	episodeNumber: number;
	episodeUrl: string;
	description: string;
	image: string;
}

interface ConstantResponse {
	genres: string[];
	seasons: string[];
	status: string[];
	mediaTypes: string[];
	watchStatus: string[];
}

// ======================== JWT ========================

interface JwtPayload {
	authorities: string[];
	exp: number;
	iat: number;
	iss: string;
	sub: string;
}
