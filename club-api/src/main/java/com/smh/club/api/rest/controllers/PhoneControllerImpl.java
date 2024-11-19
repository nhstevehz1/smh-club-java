package com.smh.club.api.rest.controllers;

import com.smh.club.api.rest.contracts.controllers.PhoneController;
import com.smh.club.api.rest.config.PagingConfig;
import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.data.contracts.services.PhoneService;
import com.smh.club.api.data.dto.PhoneDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneControllerImpl implements PhoneController {
    
    private final PhoneService phoneSvc;

    @GetMapping
    @Override
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

    @GetMapping("{id}")
    public ResponseEntity<PhoneDto> get(@PathVariable int id) {
        var ret = phoneSvc.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {

        return ResponseEntity.ok(CountResponse.of(phoneSvc.getPhoneCount()));
    }

    @PostMapping
    public ResponseEntity<PhoneDto> create(@RequestBody PhoneDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneSvc.createPhone(createDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<PhoneDto> update(@PathVariable int id, @RequestBody PhoneDto updateDto) {
        var ret = phoneSvc.updatePhone(id, updateDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        phoneSvc.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}
