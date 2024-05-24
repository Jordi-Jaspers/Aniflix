package org.jordijaspers.aniflix.email.service;

import org.jordijaspers.aniflix.api.user.model.User;
import org.jordijaspers.aniflix.email.model.MailMessage;

/**
 * An interface for sending emails.
 */
public interface EmailService {

    /**
     * Send a predefined email to the user with the validation code.
     */
    void sendUserValidationEmail(User recipient);

    /**
     * Send an email as defined in {@code mailMessage}.
     */
    void sendEmail(User recipient, MailMessage mailMessage);
}
