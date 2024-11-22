package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.hateoas.config.PagingConfig;
import com.smh.club.api.hateoas.contracts.controllers.AddressController;
import com.smh.club.api.hateoas.contracts.services.AddressService;
import com.smh.club.api.hateoas.models.AddressModel;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v2/addresses", produces = MediaTypes.HAL_JSON_VALUE)
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @Override
    public ResponseEntity<PagedModel<AddressModel>> page(
        @RequestParam(value = PagingConfig.PAGE_NAME,
            defaultValue = "${request.paging.page}") int pageNumber,
        @RequestParam(value = PagingConfig.SIZE_NAME,
            defaultValue = "${request.paging.size}") int pageSize,
        @RequestParam(value = PagingConfig.DIRECTION_NAME,
            defaultValue = "${request.paging.direction}") String sortDir,
        @RequestParam(value = PagingConfig.SORT_NAME,
            defaultValue = "") String sort) {

        log.debug("Getting page. page: {}, size: {}, direction: {}, sort: {}",
            pageNumber, pageSize, sortDir, sort);

        var page = addressService.getAddressListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(page);
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<AddressModel> get(@PathVariable int id) {
        log.debug("Getting address with id: {}", id);

        var ret = addressService.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(addressService.getAddressCount()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<AddressModel> create(AddressModel address) {
        log.debug("Creating address, data: {}", address);

        var created = addressService.createAddress(address);
        var selfLink = created.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(created);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<AddressModel> update(@PathVariable int id, @RequestBody AddressModel address) {
        log.debug("Updating address, id: {}, data: {}", id, address);

        address.setId(0);
        var ret = addressService.updateAddress(id, address);

        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<Void> delete(int id) {
        log.debug("Deleting address, id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
