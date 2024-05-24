package org.jordijaspers.aniflix.api.user.model.request;

import lombok.Data;

/**
 * The password update request.
 */
@Data
public class UpdatePasswordRequest {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    private String token;

}
