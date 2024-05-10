package org.jordijaspers.aniflix.email.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for sending emails.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application.email")
public class EmailProperties {

    /**
     * The host address of the mail server.
     */
    private String host;

    /**
     * The port of the mail server.
     */
    private Integer port;

    /**
     * The default email address to send emails from.
     */
    private String address;

    /**
     * The protocol to send mail with.
     */
    private String protocol;

    /**
     * The encoding for the messages.
     */
    private String encoding;

}
