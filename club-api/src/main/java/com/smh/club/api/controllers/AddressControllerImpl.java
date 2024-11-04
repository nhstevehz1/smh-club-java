package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.AddressController;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressControllerImpl implements AddressController {

    private final AddressService addressSvc;

    @GetMapping
    public ResponseEntity<PageResponse<AddressDto>> getItemListPage(@RequestBody PageParams pageParams) {
        if (pageParams == null) {
            pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(addressSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDto> getItem(@PathVariable int id) {
        var ret = addressSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(addressSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<AddressDto> createItem(@RequestBody AddressDto addressDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressSvc.createItem(addressDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> updateItem(@PathVariable int id, @RequestBody AddressDto addressDto) {
        var ret = addressSvc.updateItem(id, addressDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        addressSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
