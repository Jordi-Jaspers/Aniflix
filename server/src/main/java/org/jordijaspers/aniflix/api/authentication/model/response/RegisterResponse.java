package org.jordijaspers.aniflix.api.authentication.model.response;

import lombok.Data;

import java.util.List;

/**
 * A response model for user registration.
 */
@Data
public class RegisterResponse {

    private String email;

    private List<String> authorities;

    private boolean enabled;

    private boolean validated;

}
