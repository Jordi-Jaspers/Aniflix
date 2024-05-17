package org.jordijaspers.aniflix.common.mappers;

import org.jordijaspers.aniflix.common.mappers.model.PageResource;
import org.jordijaspers.aniflix.common.mappers.model.PageableItem;
import org.jordijaspers.aniflix.common.mappers.model.PageableItemResource;
import org.springframework.data.domain.Page;

import static java.util.Objects.isNull;

/**
 * Mapper superclass to convert between Paged resource objects and Paged model objects.
 *
 * @param <T> pageable item resource class.
 * @param <S> pageable item class.
 */
public abstract class PageMapper<T extends PageableItemResource, S extends PageableItem> {

    /**
     * convert PageableItem to PageableItemResource.
     */
    public abstract T toResourceObject(S source);

    /**
     * Map a page of VciServer objects to a PageResource of VciServerResource objects.
     */
    public PageResource<T> toPageResource(final Page<S> source) {
        return isNull(source) ? null : getPageResource(source);
    }

    private PageResource<T> getPageResource(final Page<S> source) {
        final PageResource<T> pageResource = new PageResource<>(
                source.getTotalElements(),
                source.getTotalPages(),
                source.getSize(),
                source.getNumber()
        );

        source.getContent().forEach(sourceObject -> pageResource.add(toResourceObject(sourceObject)));
        return pageResource;
    }
}
