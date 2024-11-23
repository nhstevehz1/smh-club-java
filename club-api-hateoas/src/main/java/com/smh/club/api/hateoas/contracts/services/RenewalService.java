package com.smh.club.api.hateoas.contracts.services;

import com.smh.club.api.hateoas.models.RenewalModel;
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

public interface RenewalService {

    PagedModel<RenewalModel> getRenewalListPage(int pageNumber, int pageSize, String direction, String sort);

    Optional<RenewalModel> getRenewal(int id);

    RenewalModel createRenewal(RenewalModel renewal);

    Optional<RenewalModel> updateRenewal(int id, RenewalModel renewal);

    void deleteRenewal(int id);

    long getRenewalCount();
}
