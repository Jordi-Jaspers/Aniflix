package org.jordijaspers.aniflix.common.mappers;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.isNull;

/**
 * Mapper that converts a local date time to a zoned date time.
 */
@Mapper
public class DateTimeMapper {

    /**
     * Converts a LocalDateTime to a ZonedDateTime, assuming zone UTC.
     */
    public ZonedDateTime localDateTimeToUTCZonedDateTime(final LocalDateTime localDateTime) {
        return isNull(localDateTime)
                ? null
                : ZonedDateTime.of(localDateTime, UTC);
    }

    /**
     * Converts an Instant to a ZonedDateTime, assuming zone UTC.
     */
    public ZonedDateTime instantToUTCZonedDateTime(final Instant instant) {
        return isNull(instant)
                ? null
                : ZonedDateTime.ofInstant(instant, UTC);
    }
}
