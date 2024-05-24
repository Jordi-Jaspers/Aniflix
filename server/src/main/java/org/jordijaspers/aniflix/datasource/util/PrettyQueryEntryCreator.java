package org.jordijaspers.aniflix.datasource.util;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * A custom query log entry creator that formats the query in a pretty way.
 */
public class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {

    private final Formatter formatter = FormatStyle.BASIC.getFormatter();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String formatQuery(final String query) {
        return this.formatter.format(query);
    }
}
