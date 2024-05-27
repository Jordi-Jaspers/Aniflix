package org.jordijaspers.aniflix.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.authentication.model.response.UserDetailsResponse;
import org.jordijaspers.aniflix.api.user.mapper.UserMapper;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.user.model.request.UpdateEmailRequest;
import org.jordijaspers.aniflix.api.user.model.request.UpdateUserDetailsRequest;
import org.jordijaspers.aniflix.api.user.service.UserService;
import org.jordijaspers.aniflix.api.user.validator.EmailValidator;
import org.jordijaspers.aniflix.security.principal.UserTokenPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.jordijaspers.aniflix.api.Paths.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The controller for the user endpoints.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final EmailValidator emailValidator;

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = USER_DETAILS, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@AuthenticationPrincipal final UserTokenPrincipal principal) {
        return ResponseEntity.status(OK).body(userMapper.toUserDetailsResponse(principal.getUser()));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = USER_DETAILS, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> updateUserDetails(@RequestBody final UpdateUserDetailsRequest request,
                                                                 @AuthenticationPrincipal final UserTokenPrincipal principal) {
        final User user = userService.updateUserDetails(principal.getUser(), request);
        return ResponseEntity.status(OK).body(userMapper.toUserDetailsResponse(user));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = USER_UPDATE_EMAIL_PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> updateUserEmail(@RequestBody final UpdateEmailRequest request,
                                                               @AuthenticationPrincipal final UserTokenPrincipal principal) {
        emailValidator.validateAndThrow(request);
        final User user = userService.updateEmail(principal.getUser(), request.getEmail());
        return ResponseEntity.status(OK).body(userMapper.toUserDetailsResponse(user));
    }

    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = VALIDATE_EMAIL_PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateEmail(@RequestBody final UpdateEmailRequest request) {
        return emailValidator.validate(request).hasErrors()
                ? ResponseEntity.status(OK).body(true)
                : ResponseEntity.status(OK).body(userService.isEmailInAlreadyUse(request.getEmail()));
    }
}
