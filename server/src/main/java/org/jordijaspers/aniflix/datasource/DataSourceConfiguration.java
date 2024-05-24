package org.jordijaspers.aniflix.datasource;

import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import org.jordijaspers.aniflix.datasource.util.PrettyQueryEntryCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static net.ttddyy.dsproxy.support.ProxyDataSourceBuilder.create;

/**
 * Configuration for ProxyDataSource with a SLF4JQueryLoggingListener.
 */
public final class DataSourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private DataSourceConfiguration() {
        // Prevent instantiation
    }

    /**
     * Builds a ProxyDataSource with a SLF4JQueryLoggingListener.
     *
     * @param dataSource the datasource to proxy
     * @param name       the name of the datasource
     * @return the ProxyDataSource
     */
    @Bean
    public static DataSource dataSource(final DataSource dataSource) {
        LOGGER.info("Adding SLF4JQueryLoggingListener to existing datasource '{}'", dataSource);
        final SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();

        final PrettyQueryEntryCreator prettyQueryEntryCreator = new PrettyQueryEntryCreator();
        prettyQueryEntryCreator.setMultiline(true);
        loggingListener.setQueryLogEntryCreator(prettyQueryEntryCreator);

        return create(dataSource).name("Aniflix-Datasource-Logger").listener(loggingListener).build();
    }
}
