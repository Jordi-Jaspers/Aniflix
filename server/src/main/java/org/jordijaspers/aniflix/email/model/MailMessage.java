package org.jordijaspers.aniflix.email.model;

import lombok.Data;

/**
 * A mail message, containing the {@code from}, {@code subject}, {@code payload} and a flag {@code isHtml}.
 */
@Data
public class MailMessage {

    /**
     * The message's sender.
     */
    private String from;

    /**
     * The message's subject.
     */
    private String subject;

    /**
     * The message's body.
     */
    private String body;

    /**
     * Flag to indicate whether the email boyd is in HTML format.
     */
    private boolean html;

}
