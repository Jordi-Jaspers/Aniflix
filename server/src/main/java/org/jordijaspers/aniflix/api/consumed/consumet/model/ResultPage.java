package org.jordijaspers.aniflix.api.consumed.consumet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A generic representation of a page of results in Consumet API.
 *
 * @param <T> the type of the results.
 */
@Data
public class ResultPage<T> {

    @JsonProperty("currentPage")
    private int currentPage;

    @JsonProperty("totalPages")
    private int totalPages;

    @JsonProperty("hasNextPage")
    private boolean hasNextPage;

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("results")
    private List<T> results = new ArrayList<>();

    /**
     * Returns the first result of the page.
     */
    public Optional<T> getFirstResult() {
        return results.stream().findFirst();
    }
}
