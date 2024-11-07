package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.EmailController;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.dto.EmailCreateDto;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.dto.EmailDto;
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
@RequestMapping(value = "emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailControllerImpl implements EmailController {
    
    private final EmailService emailSvc;

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<EmailDto>> getItemListPage(
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

        return ResponseEntity.ok(emailSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> getItem(@PathVariable int id) {
        var ret = emailSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(emailSvc.getItemCount());
    }

    @PostMapping
    @Override
    public ResponseEntity<EmailDto> createItem(@RequestBody EmailCreateDto emailDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createItem(emailDto));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> updateItem(@PathVariable int id, @RequestBody EmailCreateDto emailDto) {
        var ret = emailSvc.updateItem(id, emailDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        emailSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
