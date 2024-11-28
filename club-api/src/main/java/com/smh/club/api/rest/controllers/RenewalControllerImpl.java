package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.services.RenewalService;
import com.smh.club.api.rest.dto.RenewalDto;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smh.club.shared.api.config.PagingConfig;

/**
 * Defines REST endpoints that targets renewal objects in the database.
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalControllerImpl {
    
    private final RenewalService renewSvc;

    /**
     * Endpoint for retrieving a page of renewal objects from the database.
     *
     * @param pageNumber The page number to retrieve.
     * @param pageSize The size of the page.
     * @param sortDir The sort direction of the object list. Must be either 'ASC" or 'DESC'
     * @param sort The column name used for the sort.
     * @return A {@link ResponseEntity} containing a {@link PageResponse} of type {@link RenewalDto}.
     */
    @GetMapping
    public ResponseEntity<PageResponse<RenewalDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var page = renewSvc.getRenewalListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(PageResponse.of(page));
    }

    /**
     * Endpoint for retrieving a single renewal from the database.
     *
     * @param id The id of the renewal.
     * @return @return A {@link ResponseEntity} containing a {@link RenewalDto}
     */
    @GetMapping("{id}")
    public ResponseEntity<RenewalDto> get(@PathVariable int id) {
        var ret = renewSvc.getRenewal(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for getting the total count of addresses in the database.
     *
     * @return @return A {@link ResponseEntity} containing a {@link CountResponse}.
     */
    @GetMapping("count")
    public ResponseEntity<CountResponse> get() {

        return ResponseEntity.ok(CountResponse.of(renewSvc.getRenewalCount()));
    }

    /**
     * Endpoint for creating a renewal.
     *
     * @param renewal The {@link RenewalDto } used to create the object in the database
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} representing the newly created object.
     */
    @PostMapping
    public ResponseEntity<RenewalDto> create(@RequestBody RenewalDto renewal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createRenewal(renewal));
    }

    /**
     * Endpoint for updating a renewal.
     *
     * @param id The id of the renewal to update in the database.
     * @param renewal The {@link RenewalDto} that contains the updated info.
     * @return A {@link ResponseEntity} containing a {@link RenewalDto} that represents the updated renewal.
     */
    @PutMapping("{id}")
    public ResponseEntity<RenewalDto> update(@PathVariable int id, @RequestBody RenewalDto renewal) {
        var ret = renewSvc.updateRenewal(id, renewal);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint for deleting a renewal from the database.
     *
     * @param id The id of the renewal to delete
     * @return an empty {@link ResponseEntity}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        renewSvc.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
