package org.jordijaspers.aniflix.api.recommendation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jordijaspers.aniflix.api.anime.model.InteractionProperty;

import java.io.Serial;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The recommendation model which represents a recommendation.
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "anilistId", callSuper = false)
public class Recommendation extends InteractionProperty {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private Integer anilistId;

    private Long malId;

    private String title;

    private String status;

    private long episodes;

    private String image;

    private String cover;

    private long rating;

    private String type;

    public Recommendation(final Integer anilistId) {
        this();
        this.anilistId = anilistId;
    }
}
