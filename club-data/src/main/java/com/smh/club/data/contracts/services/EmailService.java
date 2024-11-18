package com.smh.club.data.contracts.services;

import com.smh.club.data.dto.EmailDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface EmailService {
    Page<EmailDto> getEmailListPage(int pageNumber, int pageSize, String direction, String sort);
    Optional<EmailDto> getEmail(int id);
    EmailDto createEmail(EmailDto updateDto);
    Optional<EmailDto> updateEmail(int id, EmailDto updateDto);
    void deleteEmail(int id);
    long getEmailCount();
}
