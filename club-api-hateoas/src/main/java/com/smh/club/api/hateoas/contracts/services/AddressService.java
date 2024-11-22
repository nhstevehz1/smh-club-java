package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.AddressModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface AddressService {
    PagedModel<AddressModel> getMemberListPage(int pageSize, int pageNumber, String direction, String sort);

    Optional<AddressModel> getMember(int id);

    AddressModel createMember(AddressModel member);

    Optional<AddressModel> updateMember(int id, AddressModel member);

    void deleteMember(int id);

    long getMemberCount();
}
