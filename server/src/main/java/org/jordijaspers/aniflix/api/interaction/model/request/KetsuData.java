package org.jordijaspers.aniflix.api.interaction.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KetsuData {

    private String title;

    private List<String> categories;

    public String getTitle() {
        return title.toLowerCase();
    }

}
