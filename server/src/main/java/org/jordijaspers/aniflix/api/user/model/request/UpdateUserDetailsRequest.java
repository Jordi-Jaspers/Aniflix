package org.jordijaspers.aniflix.api.user.model.request;

import lombok.Data;

/**
 * The user details update request.
 */
@Data
public class UpdateUserDetailsRequest {

    private String firstName;

    private String lastName;

}
