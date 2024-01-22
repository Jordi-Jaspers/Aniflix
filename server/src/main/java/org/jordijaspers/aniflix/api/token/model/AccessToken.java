package org.jordijaspers.aniflix.api.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AccessToken {

    private String value;

    private Instant expiresAt;

}
