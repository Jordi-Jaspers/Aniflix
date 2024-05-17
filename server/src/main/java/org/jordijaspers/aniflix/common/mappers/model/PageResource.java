package org.jordijaspers.aniflix.common.mappers.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Resource file describing a Page of elements and associated Page metadata.
 *
 * @param <T> type of items allowed in this PageResource instance.
 */
@Data
public class PageResource<T> implements Iterable<T> {

    /**
     * number of current page.
     */
    private int pageNumber;

    /**
     * total amount of pages.
     */
    private int totalPages;

    /**
     * amount of elements per page.
     */
    private int pageSize;

    /**
     * total amount of elements.
     */
    private long totalElements;

    /**
     * list of items in this page.
     */
    private List<T> content = new ArrayList<>();

    /**
     * constructor.
     *
     * @param totalElements total amount of elements.
     * @param totalPages    total amount of pages.
     * @param pageSize      amount of elements per page.
     * @param pageNumber    page number.
     * @param content       content for this page.
     */
    public PageResource(final long totalElements, final int totalPages, final int pageSize, final int pageNumber, final List<T> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.content = content;
    }

    /**
     * Constructor without content.
     */
    public PageResource(final long totalElements, final int totalPages, final int pageSize, final int pageNumber) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    /**
     * add an item to the content list.
     *
     * @param element content element.
     */
    public void add(final T element) {
        if (content == null) {
            content = new ArrayList<>();
        }
        content.add(element);
    }
}
