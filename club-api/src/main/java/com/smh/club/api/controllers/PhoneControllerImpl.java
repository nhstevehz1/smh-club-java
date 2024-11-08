package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.PhoneController;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;
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
@RequestMapping(value = "phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneControllerImpl implements PhoneController {
    
    private final PhoneService phoneSvc;

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<PhoneDto>> getItemListPage(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int page,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int size,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "default") String sort) {

        var pageParams = PageParams.builder()
                .pageNumber(page)
                .pageSize(size)
                .sortDirection(Sort.Direction.fromString(sortDir))
                .sortColumn(sort)
                .build();

        return ResponseEntity.ok(phoneSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<PhoneDto> getItem(@PathVariable int id) {
        var ret = phoneSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(phoneSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<PhoneDto> createItem(@RequestBody CreatePhoneDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneSvc.createItem(createDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<PhoneDto> updateItem(@PathVariable int id, @RequestBody UpdatePhoneDto updateDto) {
        var ret = phoneSvc.updateItem(id, updateDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        phoneSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
