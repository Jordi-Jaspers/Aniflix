package org.jordijaspers.aniflix.api.authentication.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * The request to login.
 */
@Data
@ToString
public class LoginRequest {

    private String email;

    private String password;

}
