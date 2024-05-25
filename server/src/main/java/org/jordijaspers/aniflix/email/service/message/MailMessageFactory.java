package org.jordijaspers.aniflix.email.service.message;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.config.properties.ApplicationProperties;
import org.jordijaspers.aniflix.email.config.properties.EmailProperties;
import org.jordijaspers.aniflix.email.model.MailMessage;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Mail message factory to create thymeleaf messages.
 */
@Service
@RequiredArgsConstructor
public class MailMessageFactory {

    /**
     * The Application properties.
     */
    private final ApplicationProperties properties;

    /**
     * The email sending configuration properties.
     */
    private final EmailProperties emailProperties;

    /**
     * The Thymeleaf template engine.
     */
    private final TemplateEngine templateEngine;

    /**
     * Creates a new MailMessage for the user validation.
     */
    public MailMessage createUserValidationMessage(final Map<String, Object> variables) {
        return createMessage("[Aniflix] Validate your account", "account-created", variables);
    }

    /**
     * Creates a new MailMessage for the password reset.
     */
    public MailMessage createPasswordResetMessage(final Map<String, Object> variables) {
        return createMessage("[Aniflix] Reset your password", "password-reset", variables);
    }

    /**
     * Creates a new MailMessage.
     */
    private MailMessage createMessage(final String subject, final String template, final Map<String, Object> variables) {
        variables.put("applicationUrl", properties.getUrl());

        final Context context = new Context();
        context.setVariables(variables);
        final String payload = templateEngine.process(template, context);

        final MailMessage message = new MailMessage();
        message.setFrom(emailProperties.getAddress());
        message.setSubject(subject);
        message.setBody(payload);
        message.setHtml(true);
        return message;
    }
}
