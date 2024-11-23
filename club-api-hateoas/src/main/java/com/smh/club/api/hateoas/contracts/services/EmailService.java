package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.EmailModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface EmailService {
    PagedModel<EmailModel> getEmailListPage(int pageNumber, int pageSize, String direction, String sort);

    Optional<EmailModel> getEmail(int id);

    EmailModel createEmail(EmailModel email);

    Optional<EmailModel> updateEmail(int id, EmailModel email);

    void deleteEmail(int id);

    long getEmailCount();
}
