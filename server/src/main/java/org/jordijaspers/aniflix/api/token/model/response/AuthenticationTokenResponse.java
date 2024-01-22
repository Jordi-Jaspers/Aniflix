package org.jordijaspers.aniflix.api.token.model.response;

import lombok.Data;
import org.jordijaspers.aniflix.api.token.model.Token;

@Data
public class AuthenticationTokenResponse {

    private String accessToken;

    private Token refreshToken;

    private int expiresAt;

}
