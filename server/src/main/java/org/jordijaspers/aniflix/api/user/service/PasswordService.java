package org.jordijaspers.aniflix.api.user.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.token.model.Token;
import org.jordijaspers.aniflix.api.token.model.TokenType;
import org.jordijaspers.aniflix.api.token.service.TokenService;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.user.model.request.UpdatePasswordRequest;
import org.jordijaspers.aniflix.api.user.repository.UserRepository;
import org.jordijaspers.aniflix.common.exception.PasswordIncorrectException;
import org.jordijaspers.aniflix.email.service.sender.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A service to manage the password of a user.
 */
@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final EmailService emailService;

    public void requestPasswordReset(final String email) {
        userRepository.findByEmail(email).ifPresent(emailService::sendPasswordResetEmail);
    }

    public void updatePassword(final UpdatePasswordRequest request, final User user) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordIncorrectException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void changePassword(final String password, final String value) {
        final Token token = tokenService.findPasswordResetTokenByValue(value);

        final User user = token.getUser();
        tokenService.invalidateTokensForUser(user, TokenType.RESET_PASSWORD_TOKEN);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
