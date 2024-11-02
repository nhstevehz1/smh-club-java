package com.smh.club.api.common.mappers;

import java.util.List;

public interface DataObjectMapper<T, E> {
    E toEntity(T dataObject);
    T toDataObject(E entity);
    E updateEntity(T dataObject, E entity);
    List<T> toDataObjectList(List<E> entityList);
}
