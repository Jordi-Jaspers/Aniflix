package org.jordijaspers.aniflix.common.datasource;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;

import static net.ttddyy.dsproxy.support.ProxyDataSourceBuilder.create;

/**
 * Builds a ProxyDataSource with a SLF4JQueryLoggingListener.
 */
@Configuration
public class DatasourceProxyConfiguration implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceProxyConfiguration.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Object postProcessAfterInitialization(@NonNull final Object bean, @NonNull final String beanName) throws BeansException {
        if (bean instanceof DataSource && !(bean instanceof ProxyDataSource)) {
            final HikariDataSource originalDatasource = (HikariDataSource) bean;
            final SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
            LOGGER.info("Enabling datasource proxy to log queries for datasource named '{}'", originalDatasource.getPoolName());

            final PrettyQueryEntryCreator prettyQueryEntryCreator = new PrettyQueryEntryCreator();
            prettyQueryEntryCreator.setMultiline(true);
            loggingListener.setQueryLogEntryCreator(prettyQueryEntryCreator);

            return create(originalDatasource)
                    .name("Datasource Query Logger")
                    .listener(loggingListener)
                    .build();
        }
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(@NonNull final Object bean, @NonNull final String beanName) throws BeansException {
        return bean;
    }
}
