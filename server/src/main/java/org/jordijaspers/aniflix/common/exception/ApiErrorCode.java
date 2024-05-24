package org.jordijaspers.aniflix.common.exception;

import org.hawaiiframework.exception.ApiError;

import java.util.Objects;

/**
 * Defines an error code and reason for any exception handling.
 */
public enum ApiErrorCode implements ApiError {
    INTERNAL_SERVER_ERROR(
            "ANI-0001",
            "Uncaught Exception: You think I know what went wrong here? If I did, I would've caught this exception no?"),
    INVALID_TIMESTAMP(
            "ANI-0002",
            "The provided timestamp has an incorrect format."),
    INVALID_CREDENTIALS(
            "ANI-0003",
            "Invalid Credentials: Username or password is incorrect."),
    INVALID_ID_ERROR(
            "ANI-0004",
            "The id supplied in the path is not equal to the one in the given domain object."),
    EMAIL_ADDRESS_IN_USE_ERROR(
            "ANI-0005",
            "The provided email address is already in use."),
    EMAIL_ADDRESS_DOES_NOT_FOLLOW_PATTERN(
            "ANI-0006",
            "The provided email address does not follow the OWASP confirmed pattern."),
    CANNOT_CONVERT_JSON_OR_OBJECT(
            "ANI-0007",
            "Something went wrong while converting the json to an object or vice versa."),
    PASSWORD_DOES_NOT_MATCH(
            "ANI-0008",
            "The provided password does not match the current password."),
    TOKEN_NOT_FOUND_ERROR(
            "ANI-0009",
            "Could not find the requested token."),
    INVALID_TOKEN_ERROR(
            "ANI-0010",
            "The provided JWT token is invalid."),
    USER_DISABLED_ERROR(
            "ANI-0011",
            "Authorization failed: your account has been disabled."),
    USER_UNVALIDATED_ERROR(
            "ANI-0012",
            "Authorization failed: The user must validate their email address first."),
    AUTHORIZATION_ERROR(
            "ANI-0013",
            "Username or password is incorrect. Please try again."),
    USER_ALREADY_EXISTS_ERROR(
            "ANI-0014",
            "User cannot be created: The provided email address is already in use."),
    CONSUMET_API_ERROR(
            "ANI-0015",
            "Something went wrong while trying to communicate with the Consumet API."),
    ANIME_NOT_FOUND_ERROR(
            "ANI-0016",
            "Could not find anime in the database, nor in the Consumet API."),
    NO_USER_LOGGED_IN_ERROR(
            "ANI-0017",
            "No user is logged in. Cannot perform action without authorization."),
    JIKAN_API_ERROR(
            "ANI-0018",
            "Something went wrong while trying to communicate with the Jikan API."),
    NEWS_POST_NOT_FOUND_ERROR(
            "ANI-0019",
            "Could not find the requested news post."),
    ANIME_EPISODE_NOT_FOUND_ERROR(
            "ANI-0020",
            "Could not find the requested episode of the anime."),
    ANILIST_PROVIDERS_DOWN_ERROR(
            "ANI-0021",
            "All Anilist providers are currently down. Please try again later."),
    STREAMING_LINKS_NOT_FOUND_ERROR(
            "ANI-0022",
            "Could not find the streaming links for the requested episode."),
    SYNCHRONIZATION_ERROR(
            "ANI-0023",
            "Something went wrong while synchronizing the data to the database."),
    EMAIL_ALREADY_EXISTS_ERROR(
            "ANI-0024",
            "The provided email address is already in use.");

    /**
     * The error code.
     */
    private final String errorCode;

    /**
     * The reason.
     */
    private final String reason;

    /**
     * Construct an instance with error code and reason.
     *
     * @param errorCode the error code
     * @param reason    the reason
     */
    ApiErrorCode(final String errorCode, final String reason) {
        this.errorCode = Objects.requireNonNull(errorCode);
        this.reason = Objects.requireNonNull(reason);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String getReason() {
        return reason;
    }
}
