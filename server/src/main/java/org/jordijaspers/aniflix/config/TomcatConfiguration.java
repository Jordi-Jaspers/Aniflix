package org.jordijaspers.aniflix.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class to configure the embedded tomcat instance for Spring boot with.
 */
@Configuration
@Profile("!itest")
public class TomcatConfiguration {

    /**
     * Should AJP be enabled? (default: false)
     */
    @Value("${tomcat.ajp.enabled}")
    private boolean tomcatAjpEnabled;

    /**
     * The port number Tomcat should listen on with the AJP protocol (default: 8009).
     */
    @Value("${tomcat.ajp.port}")
    private int ajpPort;

    /**
     * Whether or not the AJP secret is required (default: false).
     */
    @Value("${tomcat.ajp.secretRequired}")
    private boolean ajpSecretRequired;

    /**
     * The Tomcat AJP secret (default: secret).
     */
    @Value("${tomcat.ajp.secret}")
    private String ajpSecret;

    /**
     * Tomcat AJP/HTTP port configuration bean.
     */
    @Bean
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    public ServletWebServerFactory servletContainer() throws UnknownHostException {
        final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        if (tomcatAjpEnabled) {
            final Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setPort(ajpPort);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setSecure(false);
            ajpConnector.setScheme("http");
            final AbstractAjpProtocol<?> ajpProtocol = (AbstractAjpProtocol<?>) ajpConnector.getProtocolHandler();
            // The following allows the AJP secret to be enabled or disabled, and configured when enabled
            ajpProtocol.setSecretRequired(ajpSecretRequired);
            if (ajpSecretRequired) {
                ajpProtocol.setSecret(ajpSecret);
            }
            // The following is needed to make the AJP port listen on 0.0.0.0
            ajpProtocol.setAddress(InetAddress.getByName("0.0.0.0"));
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }
        return tomcat;
    }
}
