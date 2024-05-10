package org.jordijaspers.aniflix.api.authentication.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * The request to register a user.
 */
@Data
@ToString
public class RegisterUserRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String passwordConfirmation;

}
