package org.jordijaspers.aniflix.api.interaction.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * The most important fields of a Ketsu data object. This is a used application to watch anime on mobile devices.
 */
@Data
@EqualsAndHashCode(exclude = "title")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KetsuData {

    private String title;

    private List<String> categories;

    public String getTitle() {
        return title.toLowerCase();
    }

}
