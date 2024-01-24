package org.jordijaspers.aniflix.api.authentication.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefreshTokenRequest {

    private String refreshToken;

}
