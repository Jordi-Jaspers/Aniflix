package org.jordijaspers.aniflix.api.authentication.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.authentication.repository.RoleRepository;
import org.jordijaspers.aniflix.api.authentication.repository.UserRepository;
import org.jordijaspers.aniflix.common.exception.AuthorizationException;
import org.jordijaspers.aniflix.common.exception.UserAlreadyExistsException;
import org.jordijaspers.aniflix.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static org.jordijaspers.aniflix.api.authentication.model.Authority.USER;
import static org.jordijaspers.aniflix.common.exception.ApiErrorCode.*;

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
                .orElseThrow(() -> new AuthorizationException(USER_NOT_FOUND_ERROR));
    }

    public User updateUserDetails(final User user) {
        return userRepository.save(user);
    }

    public User registerUser(final User user, final String password) {
        try {
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(List.of(roleRepository.findByAuthority(USER).orElseThrow()));
            user.setEnabled(true);
            user.setValidated(false);
            final User persistedUser = userRepository.save(user);

            LOGGER.info("User has been registered, sending email to validate account.");
            emailService.sendUserValidationEmail(persistedUser);
            return persistedUser;
        } catch (final DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException(exception);
        }
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
}
