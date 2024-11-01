package com.smh.club.api.common.services;

import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface CrudService<T> {
    PageResponse<T> getItemListPage(PageParams pageParams);
    Optional<T> getItem(int id);
    T createItem(T item);
    Optional<T> updateItem(int id, T item);
    void deleteItem(int id);
    CountResponse getItemCount();
}
