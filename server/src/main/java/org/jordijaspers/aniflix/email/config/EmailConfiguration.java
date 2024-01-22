package org.jordijaspers.aniflix.email.config;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.email.config.properties.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

    /**
     * The email sending configuration properties.
     */
    private final EmailProperties emailProperties;

    /**
     * The JavaMailSender definition.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());
        mailSender.setProtocol(emailProperties.getProtocol());
        mailSender.setDefaultEncoding(emailProperties.getEncoding());
        mailSender.getJavaMailProperties().put("mail.transport.protocol", emailProperties.getProtocol());
        return mailSender;
    }

}
