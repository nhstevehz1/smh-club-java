package com.smh.club.api.common.services;

import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.dto.update.UpdateEmailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface EmailService {
    PageResponse<EmailDto> getItemListPage(PageParams pageParams);
    Optional<EmailDto> getItem(int id);
    EmailDto createItem(CreateEmailDto updateDto);
    Optional<EmailDto> updateItem(int id, UpdateEmailDto updateDto);
    void deleteItem(int id);
    CountResponse getItemCount();
}
