package com.smh.club.api.hateoas.services;

import org.springframework.hateoas.PagedModel;

import java.util.List;

public abstract class ServiceTests {

    protected <T> PagedModel<T> createdPageModel(List<T> content ) {;
        PagedModel.PageMetadata md =
            new PagedModel.PageMetadata(content.size(), 5, 200,  20) ;

        return PagedModel.of(content, md);
    }
}
