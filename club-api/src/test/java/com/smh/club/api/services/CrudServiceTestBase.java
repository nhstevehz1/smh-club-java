package com.smh.club.api.services;

import com.smh.club.api.request.PageParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public abstract class CrudServiceTestBase<D, E> {

    protected abstract E createEntity(int flag);

    protected  List<E> createEntityList(int size) {
        List<E> list = new ArrayList<>();
        for (int ii = 0; ii < size; ii++) {
            list.add(createEntity(ii));
        }
        return list;
    }

    protected abstract D createDataObject(int flag);

    protected List<D> createDataObjectList(int size) {
        List<D> list = new ArrayList<>();
        for (int ii = 0; ii < size; ii++) {
            list.add(createDataObject(ii));
        }
        return list;
    }

    protected Page<E> createPage(int size, Pageable pageable, int total) {
        return new PageImpl<E>(createEntityList(size), pageable, total);
    }

    protected PageParams createPageParam(int pageNumber, int pageSize, Sort.Direction direction, String column) {
        return PageParams.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortDirection(direction)
                .sortColumn(column)
                .build();
    }


}
