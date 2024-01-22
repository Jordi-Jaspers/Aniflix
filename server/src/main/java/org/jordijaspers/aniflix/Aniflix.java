package org.jordijaspers.aniflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * The main class of the application.
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Aniflix extends SpringBootServletInitializer {

    /**
     * The default timezone to use.
     */
    public static final String DEFAULT_TIMEZONE = "UTC";

    /**
     * The main method to run the spring boot application.
     */
    public static void main(final String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        SpringApplication.run(Aniflix.class, args);
    }
}
