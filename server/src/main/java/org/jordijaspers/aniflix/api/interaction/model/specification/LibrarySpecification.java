package org.jordijaspers.aniflix.api.interaction.model.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.Anime;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.genre.model.Genre;
import org.jordijaspers.aniflix.api.interaction.model.Interaction;
import org.jordijaspers.aniflix.api.interaction.model.request.LibrarySearchRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The specification for the library search. Which will filter the anime based on the request.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
@RequiredArgsConstructor
public class LibrarySpecification implements Specification<Anime> {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private final LibrarySearchRequest request;

    private final User user;

    @Override
    public Predicate toPredicate(@NonNull final Root<Anime> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        // Configure The Query filter
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + request.getQuery() + "%"));
        }

        // Configure The Rating filter
        if (request.getMinRating() > 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), request.getMinRating()));
        }
        if (request.getMaxRating() > 0) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), request.getMaxRating()));
        }

        // Configure The Release Year filter
        if (request.getBeforeYear() > 0 && request.getBeforeYear() <= now().getYear()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("releaseYear"), request.getBeforeYear()));
        }
        if (request.getAfterYear() > 0 && request.getAfterYear() <= now().getYear()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("releaseYear"), request.getAfterYear()));
        }

        // Configure The Genre filter
        if (isNotEmpty(request.getGenre())) {
            final List<Genre> genreList = request.getGenre().stream()
                    .map(Genre::new)
                    .toList();
            predicates.add(root.join("genres").in(genreList));
        }

        // Configure The Watch Status filter
        if (isNotEmpty(request.getWatchStatus())) {
            predicates.add(root.get("watchStatus").in(request.getWatchStatus()));
        }

        // Configure The Status filter
        if (isNotEmpty(request.getStatus())) {
            predicates.add(root.get("status").in(request.getStatus()));
        }

        // Join with Interaction table and filter by inLibrary and userId
        final Join<Anime, Interaction> interactionJoin = root.join("interactions");
        predicates.add(criteriaBuilder.equal(interactionJoin.get("inLibrary"), true));
        predicates.add(criteriaBuilder.equal(interactionJoin.get("user"), user));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
