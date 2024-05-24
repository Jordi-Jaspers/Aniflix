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
 * A service to manage users, their registration and authentication. It also implements the {@link UserDetailsService}
 * to load users by their username. So, Spring Security can use this service to authenticate users.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public User loadUserByUsername(final String username) {
        return userRepository.findByEmail(username)
                .map(user -> {
                    if (!user.isEnabled()) {
                        throw new AuthorizationException(USER_DISABLED_ERROR);
                    } else if (!user.isValidated()) {
                        throw new AuthorizationException(USER_UNVALIDATED_ERROR);
                    }
                    user.setLastLogin(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AuthorizationException(INVALID_CREDENTIALS));
    }

    public User updateUserDetails(final User user, final UpdateUserDetailsRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return updateUserDetails(user);
    }

    public User updateUserDetails(final User user) {
        return userRepository.save(user);
    }

    public User UpdateEmail(final User user, final String email) {
        LOGGER.info("Attempting to update email for '{}'", user.getEmail());
        if (isEmailInAlreadyUse(email)) {
            throw new EmailAlreadyExistsException();
        }

        user.setEmail(email);
        user.setValidated(false);

        LOGGER.info("Email has been updated to '{}', sending email to validate account.", email);
        emailService.sendUserValidationEmail(user);
        return userRepository.save(user);
    }

    public boolean isEmailInAlreadyUse(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void resendValidationEmail(final String email) {
        LOGGER.info("Attempting to resend validation email to '{}'", email);
        final User user = userRepository.findByEmail(email).orElse(null);
        if (isNull(user) || user.isValidated()) {
            LOGGER.error("User '{}' not found or already validated", email);
        } else {
            emailService.sendUserValidationEmail(user);
        }
    }

    public User registerAndNotify(final User newUser, final String password) {
        LOGGER.info("Attempting to register new user '{}'", newUser.getEmail());
        final User existingUser = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        if (nonNull(existingUser)) {
            if (existingUser.isValidated()) {
                resendValidationEmail(existingUser.getEmail());
                return existingUser;
            }
            LOGGER.error("User '{}' already exists", newUser.getEmail());
            throw new UserAlreadyExistsException();
        }

        final User user = register(newUser, password);
        LOGGER.info("User has been registered, sending email to validate account.");
        emailService.sendUserValidationEmail(user);
        return user;
    }

    private User register(final User newUser, final String password) {
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(List.of(roleRepository.findByAuthority(USER).orElseThrow()));
        newUser.setEnabled(true);
        newUser.setValidated(false);
        return userRepository.save(newUser);
    }
}
