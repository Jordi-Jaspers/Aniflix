package org.jordijaspers.aniflix.email.service;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.api.token.model.Token;
import org.jordijaspers.aniflix.api.token.model.TokenType;
import org.jordijaspers.aniflix.api.token.service.TokenService;
import org.jordijaspers.aniflix.email.model.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * A service to send emails in a development environment.
 */
@Service
@Profile("development")
@RequiredArgsConstructor
public class DevelopmentEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final TokenService tokenService;

    @Override
    public void sendEmail(final User recipient, final MailMessage mailMessage) {
        LOGGER.info("Sending email to user '{}' with content: \n{}", recipient.getEmail(), mailMessage);
    }

    /**
     * Send an email as defined in {@code mailMessage}.
     * <p>
     * See {@link MailMessage}.
     */
    @Override
    public void sendUserValidationEmail(final User recipient) {
        final Token token = tokenService.generateToken(recipient, TokenType.USER_VALIDATION_TOKEN);
        LOGGER.info("Sending validation code '{}' to user with email address '{}'", token.getValue(), recipient.getEmail());
    }
}
