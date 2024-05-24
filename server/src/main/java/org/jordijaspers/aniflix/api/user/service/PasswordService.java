package org.jordijaspers.aniflix.api.user.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.authentication.repository.RoleRepository;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.user.model.request.UpdateUserDetailsRequest;
import org.jordijaspers.aniflix.api.user.repository.UserRepository;
import org.jordijaspers.aniflix.common.exception.AuthorizationException;
import org.jordijaspers.aniflix.common.exception.EmailAlreadyExistsException;
import org.jordijaspers.aniflix.common.exception.UserAlreadyExistsException;
import org.jordijaspers.aniflix.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.jordijaspers.aniflix.api.authentication.model.Authority.USER;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.*;

/**
 * A service to manage the password of a user.
 */
@Service
@RequiredArgsConstructor
public class PasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

}
