package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.controllers.RenewalController;
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

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalControllerImpl implements RenewalController {
    
    private final RenewalService renewSvc;
    
    @GetMapping
    @Override
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

    @GetMapping("{id}")
    @Override
    public ResponseEntity<RenewalDto> get(@PathVariable int id) {
        var ret = renewSvc.getRenewal(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> get() {

        return ResponseEntity.ok(CountResponse.of(renewSvc.getRenewalCount()));
    }

    @PostMapping
    @Override
    public ResponseEntity<RenewalDto> create(@RequestBody RenewalDto renewal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createRenewal(renewal));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<RenewalDto> update(@PathVariable int id, @RequestBody RenewalDto renewal) {
        var ret = renewSvc.updateRenewal(id, renewal);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        renewSvc.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
