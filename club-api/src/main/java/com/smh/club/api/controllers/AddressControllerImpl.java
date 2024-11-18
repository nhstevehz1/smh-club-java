package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.AddressController;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.data.dto.AddressDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
                    defaultValue = "${request.paging.page}") int page,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int size,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value= PagingConfig.SORT_NAME, required = false) String sort) {

        var pageParams = PageParams.builder()
                .pageNumber(page)
                .pageSize(size)
                .sortDirection(Sort.Direction.fromString(sortDir))
                .sortColumn(sort)
                .build();

        return ResponseEntity.ok(addressSvc.getAddressListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDto> get(@PathVariable int id) {
        var ret = addressSvc.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(addressSvc.getAddressCount());
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
