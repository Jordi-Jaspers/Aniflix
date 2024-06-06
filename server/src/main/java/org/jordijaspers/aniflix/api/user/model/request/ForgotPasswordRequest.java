package org.jordijaspers.aniflix.api.user.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The password update request.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ForgotPasswordRequest extends PasswordRequest {

    private String token;

}
