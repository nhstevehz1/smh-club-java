package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.PhoneService;
import com.smh.club.api.rest.dto.PhoneDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.config.PagingConfig;

/**
 * Defines REST endpoints that targets phone objects in the database.
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping(value = "/api/v1/phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneController {
    
    private final PhoneService phoneSvc;

    /**
     * Endpoint for retrieving a page of phone objects from the database.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link PhoneDto}.
     */
    @GetMapping
    public ResponseEntity<PageResponse<PhoneDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var page = phoneSvc.getPhoneListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(PageResponse.of(page));
    }

    /**
     * Endpoint for retrieving a single phone from the database.
     *
     * @param id The id of the phone.
     * @return @return A {@link ResponseEntity} containing a {@link PhoneDto}
     */
    @GetMapping("{id}")
    public ResponseEntity<PhoneDto> get(@PathVariable int id) {
        var ret = phoneSvc.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of phonees in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {

        return ResponseEntity.ok(CountResponse.of(phoneSvc.getPhoneCount()));
    }

    /**
     * Endpoint for creating a phone.
     * @param phone The {@link PhoneDto} used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} representing the newly created object.
     */
    @PostMapping
    public ResponseEntity<PhoneDto> create(@RequestBody PhoneDto phone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneSvc.createPhone(phone));
    }

    /**
     * Endpoint for updating a phone.
     *
     * @param id The id of the phone to update in the database.
     * @param phone The {@link PhoneDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link PhoneDto} that represents the updated phone.
     */
    @PutMapping("{id}")
    public ResponseEntity<PhoneDto> update(@PathVariable int id, @RequestBody PhoneDto phone) {
        var ret = phoneSvc.updatePhone(id, phone);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a phone from the database.
     *
     * @param id The id of the phone to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        phoneSvc.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
