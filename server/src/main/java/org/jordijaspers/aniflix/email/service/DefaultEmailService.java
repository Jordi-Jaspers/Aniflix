package org.jordijaspers.aniflix.email.service;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.exception.HawaiiException;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.email.model.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * Service (infrastructure) to send emails with.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(EmailService.class)
public class DefaultEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

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
            LOGGER.debug("Sending email to '{}' with subject '{}'.", recipient, mailMessage.getSubject());
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new HawaiiException("Error sending email.", e);
        }
    }

    @Override
    public void sendUserValidationEmail(final User recipient) {
        LOGGER.error("This feature is not implemented yet.");
    }
}
