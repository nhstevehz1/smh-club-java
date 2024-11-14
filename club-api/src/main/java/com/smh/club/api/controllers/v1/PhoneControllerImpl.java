package com.smh.club.api.controllers.v1;

import com.smh.club.api.common.controllers.v1.PhoneController;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.dto.PhoneDto;
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
@RequestMapping(value = "/api/v1/phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneControllerImpl implements PhoneController {
    
    private final PhoneService phoneSvc;

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<PhoneDto>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int page,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int size,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME, required = false) String sort) {

        var pageParams = PageParams.builder()
                .pageNumber(page)
                .pageSize(size)
                .sortDirection(Sort.Direction.fromString(sortDir))
                .sortColumn(sort)
                .build();

        return ResponseEntity.ok(phoneSvc.getPhoneListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<PhoneDto> get(@PathVariable int id) {
        var ret = phoneSvc.getPhone(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(phoneSvc.getPhoneCount());
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
