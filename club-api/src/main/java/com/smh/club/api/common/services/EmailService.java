package com.smh.club.api.common.services;

import com.smh.club.api.dto.EmailCreateDto;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface EmailService {
    PageResponse<EmailDto> getItemListPage(PageParams pageParams);
    Optional<EmailDto> getItem(int id);
    EmailDto createItem(EmailCreateDto email);
    Optional<EmailDto> updateItem(int id, EmailCreateDto emailDto);

    void deleteItem(int id);

    CountResponse getItemCount();
}
