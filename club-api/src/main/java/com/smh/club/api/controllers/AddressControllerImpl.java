package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.AddressController;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.request.PageParams;
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
@RequestMapping(value = "addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressControllerImpl implements AddressController {

    private final AddressService addressSvc;

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<AddressDto>> getAddressListPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir,
            @RequestParam(value = "sort", defaultValue = "default") String sort) {

        var pageParams = PageParams.builder()
                .pageNumber(page)
                .pageSize(size)
                .sortDirection(Sort.Direction.fromString(sortDir))
                .sortColumn(sort)
                .build();

        return ResponseEntity.ok(addressSvc.getAddressListPage(pageParams));
    }

    @Override
    @GetMapping("{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable int id) {
        var ret = addressSvc.getAddress(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(addressSvc.getAddressCount());
    }

    @Override
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressCreateDto address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressSvc.createAddress(address));
    }

    @Override
    @PutMapping("{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable int id, @RequestBody AddressCreateDto address) {
        var ret = addressSvc.updateAddress(id, address);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable int id) {
        addressSvc.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
