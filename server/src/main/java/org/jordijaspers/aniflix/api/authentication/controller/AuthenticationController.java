package org.jordijaspers.aniflix.api.authentication.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.user.mapper.UserMapper;
import org.jordijaspers.aniflix.api.authentication.model.request.LoginRequest;
import org.jordijaspers.aniflix.api.authentication.model.request.RefreshTokenRequest;
import org.jordijaspers.aniflix.api.authentication.model.request.RegisterUserRequest;
import org.jordijaspers.aniflix.api.authentication.model.response.RegisterResponse;
import org.jordijaspers.aniflix.api.authentication.model.response.UserResponse;
import org.jordijaspers.aniflix.api.authentication.service.AuthenticationService;
import org.jordijaspers.aniflix.api.authentication.validator.AuthenticationValidator;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the authentication.
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AuthenticationValidator validator;

    private final UserMapper userMapper;

    @ResponseStatus(CREATED)
    @PostMapping(path = REGISTER_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> register(@RequestBody final RegisterUserRequest request) {
        validator.validateUserRegistration(request);
        final User user = authenticationService.register(userMapper.toUser(request), request.getPassword());
        return ResponseEntity.status(CREATED).body(userMapper.toRegisterResponse(user));
    }

    @ResponseStatus(OK)
    @PostMapping(path = AUTHORIZE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> authorize(@RequestBody final LoginRequest request) {
        validator.validateLoginRequest(request);
        final User user = authenticationService.authorize(request.getEmail(), request.getPassword());
        return ResponseEntity.status(OK).body(userMapper.toUserResponse(user));
    }

    @ResponseStatus(OK)
    @PostMapping(path = TOKEN_PATH)
    public ResponseEntity<UserResponse> refreshTokens(@RequestBody final RefreshTokenRequest request) {
        final User user = authenticationService.refresh(request.getRefreshToken());
        return ResponseEntity.status(OK).body(userMapper.toUserResponse(user));
    }

    @ResponseStatus(NO_CONTENT)
    @GetMapping(path = LOGOUT_PATH)
    public ResponseEntity<Void> logout(@AuthenticationPrincipal final UserTokenPrincipal principal) {
        if (nonNull(principal)) {
            authenticationService.logout(principal.getUser());
        }
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ResponseStatus(OK)
    @PostMapping(path = USER_VALIDATION_PATH)
    public ResponseEntity<UserResponse> validateUser(@RequestParam("token") final String token) {
        final User user = authenticationService.validate(token);
        return ResponseEntity.status(OK).body(userMapper.toUserResponse(user));
    }

    @ResponseStatus(NO_CONTENT)
    @PostMapping(path = RESEND_VALIDATION_PATH)
    public ResponseEntity<Void> resendValidationEmail(@RequestParam("email") final String email) {
        authenticationService.resendValidation(email);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
