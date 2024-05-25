package org.jordijaspers.aniflix.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.user.model.request.UpdatePasswordRequest;
import org.jordijaspers.aniflix.api.user.service.PasswordService;
import org.jordijaspers.aniflix.api.user.validator.ChangePasswordValidator;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the password endpoints.
 */
@RestController
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    private final ChangePasswordValidator passwordValidator;

    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = UPDATE_PASSWORD_PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassword(@RequestBody final UpdatePasswordRequest request,
                                            @AuthenticationPrincipal final UserTokenPrincipal principal) {
        passwordValidator.validateAndThrow(request);
        passwordService.changePassword(request.getNewPassword(), principal.getTokenValue());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @GetMapping(path = PUBLIC_REQUEST_PASSWORD_RESET_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> requestPasswordReset(@RequestParam(name = "email") final String email) {
        passwordService.requestPasswordReset(email);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping(path = PUBLIC_RESET_PASSWORD_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody final UpdatePasswordRequest request) {
        passwordValidator.validateAndThrow(request);
        passwordService.changePassword(request.getNewPassword(), request.getToken());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
