package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.PhoneModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface PhoneService {

    PagedModel<PhoneModel> getPhoneListPage(int pageNumber, int pageSize, String direction, String sort);

    Optional<PhoneModel> getPhone(int id);

    PhoneModel createPhone(PhoneModel phone);

    Optional<PhoneModel> updatePhone(int id, PhoneModel phone);

    void deletePhone(int id);

    long getPhoneCount();
}
