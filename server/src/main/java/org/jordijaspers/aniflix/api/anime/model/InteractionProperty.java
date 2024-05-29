package org.jordijaspers.aniflix.api.anime.model;

import jakarta.persistence.Transient;
import lombok.Data;
import org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus;
import org.jordijaspers.aniflix.common.mappers.model.PageableItem;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.jordijaspers.aniflix.api.anime.model.constant.WatchStatus.NOT_STARTED;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * Marker interface for domain objects which can be interacted with.
 */
@Data
public abstract class InteractionProperty implements Serializable, PageableItem {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    @Transient
    private boolean liked;

    @Transient
    private boolean inLibrary;

    @Transient
    private int lastSeenEpisode;

    @Transient
    private WatchStatus watchStatus = NOT_STARTED;

    @Transient
    private LocalDateTime lastInteraction;

    protected InteractionProperty() {
        // no-args constructor
    }
}
