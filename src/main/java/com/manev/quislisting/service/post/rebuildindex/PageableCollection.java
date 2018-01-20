package com.manev.quislisting.service.post.rebuildindex;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PageableCollection<T> implements Iterable<T> {
    private static final int DEFAULT_PAGE_SIZE = 100;

    private Fetcher<?, T> fetcher;
    private int pageSize;

    PageableCollection(Fetcher<?, T> f) {
        this(f, DEFAULT_PAGE_SIZE);
    }

    private PageableCollection(Fetcher<?, T> f, int pageSize) {
        this.fetcher = f;
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<T> iterator() {
        return new PageableIterator<>(fetcher, pageSize);
    }
}

/**
 * Initially makes sense only inside the PageableCollection class
 */
class PageableIterator<T> implements Iterator<T> {
    private static final int FIRST_PAGE = 0;

    private List<T> currentData;
    private int cursor;
    private int pageSize;
    private Pageable page;
    private Fetcher<?, T> fetcher;

    PageableIterator(Fetcher<?, T> f, int pageSize) {
        this.fetcher = f;
        this.pageSize = pageSize;
        page = new PageRequest(FIRST_PAGE, pageSize);
        currentData = new ArrayList<>();
    }

    @Override
    public boolean hasNext() {
        if (hasDataLoaded()) {
            return true;
        }

        tryToFetchMoreData();
        return !currentData.isEmpty();
    }

    private void tryToFetchMoreData() {
        currentData = fetcher.fetch(page);
        page = new PageRequest(page.getPageNumber() + 1, pageSize);
        cursor = 0;
    }

    private boolean hasDataLoaded() {
        return cursor < currentData.size();
    }

    @Override
    public T next() {
        try{
            return currentData.get(cursor++);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

