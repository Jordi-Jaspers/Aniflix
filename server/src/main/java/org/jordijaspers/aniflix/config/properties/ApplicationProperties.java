package org.jordijaspers.aniflix.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class to configure the embedded tomcat instance for Spring boot with.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "application.properties")
public class ApplicationProperties {

    /**
     * The name of the application.
     */
    private String name;

    /**
     * The current version of the application.
     */
    private String version;

    /**
     * The default url of the application.
     */
    private String url;

    /**
     * The description of the application.
     */
    private String description;

}
