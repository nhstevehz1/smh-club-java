package com.smh.club.api.common.services;

import com.smh.club.data.dto.EmailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;

import java.util.Optional;

public interface EmailService {
    PageResponse<EmailDto> getEmailListPage(PageParams pageParams);
    Optional<EmailDto> getEmail(int id);
    EmailDto createEmail(EmailDto updateDto);
    Optional<EmailDto> updateEmail(int id, EmailDto updateDto);
    void deleteEmail(int id);
    CountResponse getEmailCount();
}
