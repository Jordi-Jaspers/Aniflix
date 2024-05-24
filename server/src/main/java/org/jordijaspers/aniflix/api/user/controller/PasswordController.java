package org.jordijaspers.aniflix.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.user.mapper.UserMapper;
import org.jordijaspers.aniflix.api.user.model.request.UpdatePasswordRequest;
import org.jordijaspers.aniflix.api.user.service.PasswordService;
import org.jordijaspers.aniflix.api.user.validator.ChangePasswordValidator;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the password endpoints.
 */
@RestController
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    private final ChangePasswordValidator passwordValidator;

    private final UserMapper userMapper;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = CHANGE_PASSWORD, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody final UpdatePasswordRequest request,
                                            @AuthenticationPrincipal final UserTokenPrincipal principal) {
        passwordValidator.validateAndThrow(request);
        return ResponseEntity.status(OK).body("");
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping(path = PUBLIC_REQUEST_PASSWORD_RESET_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> requestPasswordReset(@RequestParam(name = "email") final String email) {
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(OK)
    @PostMapping(path = PUBLIC_RESET_PASSWORD_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forcePasswordReset(@RequestBody final UpdatePasswordRequest request) {
        passwordValidator.validateAndThrow(request);
        return ResponseEntity.status(OK).body("");
    }
}
