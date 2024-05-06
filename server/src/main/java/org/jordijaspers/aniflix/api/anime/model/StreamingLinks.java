package org.jordijaspers.aniflix.api.anime.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamingLinks implements Serializable {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private String referer;

    private List<StreamingSource> sources;

}
