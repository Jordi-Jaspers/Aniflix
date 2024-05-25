package org.jordijaspers.aniflix.email.service.sender;

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
     * Sends an email containing a url with a token to reset the password.
     */
    void sendPasswordResetEmail(User recipient);

    /**
     * Send an email as defined in {@code mailMessage}.
     */
    void sendEmail(User recipient, MailMessage mailMessage);
}
