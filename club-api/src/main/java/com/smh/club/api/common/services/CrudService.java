package com.smh.club.api.common.services;

import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

public interface CrudService<T> {
    PageResponse<T> getItemListPage(PageParams pageParams);
    T getItem(int id);
    T createItem(T item);
    T updateItem(int id, T item);
    void deleteItem(int id);
    CountResponse getItemCount();
}
