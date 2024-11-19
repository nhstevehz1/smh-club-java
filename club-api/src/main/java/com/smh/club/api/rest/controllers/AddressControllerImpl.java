package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.controllers.AddressController;
import com.smh.club.api.rest.config.PagingConfig;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.rest.contracts.services.AddressService;
import com.smh.club.api.data.dto.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressControllerImpl implements AddressController {

    private final AddressService addressSvc;

    @GetMapping
    public ResponseEntity<PageResponse<AddressDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value= PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var page = addressSvc.getAddressListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(PageResponse.of(page));
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDto> get(@PathVariable int id) {
        var ret = addressSvc.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {

        return ResponseEntity.ok(CountResponse.of(addressSvc.getAddressCount()));
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(@RequestBody AddressDto address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressSvc.createAddress(address));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> update(@PathVariable int id, @RequestBody AddressDto address) {
        var ret = addressSvc.updateAddress(id, address);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        addressSvc.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
