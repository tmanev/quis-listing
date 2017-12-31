package com.manev.quislisting.service.post.rebuildindex;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class Fetcher<S, T>
{
    protected S source;

    public Fetcher(S s) {
        this.source = s;
    }

    public abstract List<T> fetch(Pageable pageRequest);
}