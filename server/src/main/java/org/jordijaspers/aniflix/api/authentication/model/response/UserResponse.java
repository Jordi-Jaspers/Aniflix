package org.jordijaspers.aniflix.api.authentication.model.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * The response of a user.
 */
@Data
public class UserResponse {

    private String email;

    private List<String> authorities;

    private ZonedDateTime lastLogin;

    private String accessToken;

    private String refreshToken;

    private ZonedDateTime expiresAt;

    private boolean enabled;

    private boolean validated;

}
