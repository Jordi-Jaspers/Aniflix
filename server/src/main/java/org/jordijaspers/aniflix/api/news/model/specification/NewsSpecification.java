package org.jordijaspers.aniflix.api.news.model.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jordijaspers.aniflix.api.anime.model.request.PageableRequest;
import org.jordijaspers.aniflix.api.news.model.NewsPost;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static org.jordijaspers.aniflix.config.GlobalConfiguration.SERIAL_VERSION_UID;

/**
 * The specification for the library search. Which will filter the anime based on the request.
 */
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
@RequiredArgsConstructor
public class NewsSpecification implements Specification<NewsPost> {

    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    private final PageableRequest request;

    @Override
    public Predicate toPredicate(@NonNull final Root<NewsPost> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        // Sort them by uploadedAt
        query.orderBy(criteriaBuilder.desc(root.get("uploadedAt")));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
