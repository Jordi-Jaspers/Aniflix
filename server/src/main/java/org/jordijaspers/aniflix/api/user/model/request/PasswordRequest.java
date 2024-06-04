package org.jordijaspers.aniflix.api.user.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The password update request.
 */
@Data
public class PasswordRequest {

    private String newPassword;

    private String confirmPassword;

}
