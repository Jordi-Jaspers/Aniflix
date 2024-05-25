package org.jordijaspers.aniflix.api.token.model;

import lombok.Getter;

/**
 * The type of the token which is used to identify the purpose of the token.
 */
@Getter
public enum TokenType {

    ACCESS_TOKEN,
    REFRESH_TOKEN,
    USER_VALIDATION_TOKEN,
    RESET_PASSWORD_TOKEN;

    private String type;

    public boolean isRefreshToken() {
        return this == REFRESH_TOKEN;
    }

    public boolean isAccessToken() {
        return this == ACCESS_TOKEN;
    }

}
