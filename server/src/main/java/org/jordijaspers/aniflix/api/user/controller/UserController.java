package org.jordijaspers.aniflix.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.authentication.model.mapper.UserMapper;
import org.jordijaspers.aniflix.api.authentication.model.response.UserDetailsResponse;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.USER_DETAILS;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the user endpoints.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = USER_DETAILS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@AuthenticationPrincipal final UserTokenPrincipal principal) {
        return ResponseEntity.status(OK).body(userMapper.toUserDetailsResponse(principal.getUser()));
    }
}
