package com.smh.club.api.common.controllers;

import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface CrudController<T> {
    ResponseEntity<PageResponse<T>> getItemListPage(PageParams pageParams);
    ResponseEntity<T> getItem(int id);
    ResponseEntity<CountResponse> getCount();
    ResponseEntity<T> createItem(T data);
    ResponseEntity<T> updateItem(int id, T data);
    ResponseEntity<Void> deleteItem(int id);
}
