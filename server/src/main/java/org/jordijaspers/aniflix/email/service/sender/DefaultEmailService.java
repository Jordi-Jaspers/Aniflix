package org.jordijaspers.aniflix.email.service.sender;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.exception.HawaiiException;
import org.jordijaspers.aniflix.api.token.model.Token;
import org.jordijaspers.aniflix.api.token.model.TokenType;
import org.jordijaspers.aniflix.api.token.service.TokenService;
import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.email.model.MailMessage;
import org.jordijaspers.aniflix.email.service.message.MailMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service (infrastructure) to send emails with.
 */
@Service
@RequiredArgsConstructor
@Profile("!development")
public class DefaultEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final TokenService tokenService;

    private final JavaMailSender mailSender;

    private final MailMessageFactory mailMessageFactory;

    @Override
    public void sendUserValidationEmail(final User recipient) {
        LOGGER.info("Sending user validation email to '{}'.", recipient.getEmail());
        final Map<String, Object> variables = new ConcurrentHashMap<>();

        final Token token = tokenService.generateToken(recipient, TokenType.USER_VALIDATION_TOKEN);
        variables.put("token", token.getValue());
        final MailMessage message = mailMessageFactory.createUserValidationMessage(variables);
        sendEmail(recipient, message);
    }

    @Override
    public void sendPasswordResetEmail(final User recipient) {
        LOGGER.info("Sending password reset email to '{}'.", recipient.getEmail());
        final Map<String, Object> variables = new ConcurrentHashMap<>();

        final Token resetToken = tokenService.generateToken(recipient, TokenType.RESET_PASSWORD_TOKEN);
        variables.put("token", resetToken.getValue());
        final MailMessage message = mailMessageFactory.createPasswordResetMessage(variables);
        sendEmail(recipient, message);
    }

    @Override
    public void sendEmail(final User recipient, final MailMessage mailMessage) {
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailMessage.getFrom());
            messageHelper.setTo(recipient.getEmail());
            messageHelper.setSubject(mailMessage.getSubject());
            messageHelper.setText(mailMessage.getBody(), mailMessage.isHtml());
        };
        try {
            LOGGER.debug("Sending email to '{}' with subject '{}'.", recipient.getEmail(), mailMessage.getSubject());
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new HawaiiException("Error sending email.", e);
        }
    }
}
