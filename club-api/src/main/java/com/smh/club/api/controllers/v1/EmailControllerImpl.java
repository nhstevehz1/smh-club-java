package com.smh.club.api.controllers.v1;

import com.smh.club.api.common.controllers.v1.EmailController;
import com.smh.club.api.common.services.EmailService;
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
@RequestMapping(value = "/api/v1/emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailControllerImpl implements EmailController {
    
    private final EmailService emailSvc;

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<EmailDto>> page(
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

        return ResponseEntity.ok(emailSvc.getEmailListPage(pageParams));
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> get(@PathVariable int id) {
        var ret = emailSvc.getEmail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(emailSvc.getEmailCount());
    }

    @PostMapping
    @Override
    public ResponseEntity<EmailDto> create(@RequestBody EmailDto emailDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createEmail(emailDto));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<EmailDto> update(@PathVariable int id, @RequestBody EmailDto emailDto) {
        var ret = emailSvc.updateEmail(id, emailDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        emailSvc.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }
}
