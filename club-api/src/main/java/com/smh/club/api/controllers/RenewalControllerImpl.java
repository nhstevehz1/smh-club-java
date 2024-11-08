package com.smh.club.api.controllers;

import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
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
@RequestMapping(value = "renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalControllerImpl implements com.smh.club.api.common.controllers.RenewalController {
    
    private final RenewalService renewSvc;
    
    @GetMapping
    @Override
    public ResponseEntity<PageResponse<RenewalDto>> getItemListPage(
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

        return ResponseEntity.ok(renewSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<RenewalDto> getItem(@PathVariable int id) {
        var ret = renewSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(renewSvc.getItemCount());
    }

    @PostMapping
    @Override
    public ResponseEntity<RenewalDto> createItem(@RequestBody CreateRenewalDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createItem(createDto));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<RenewalDto> updateItem(@PathVariable int id, @RequestBody UpdateRenewalDto updateDto) {
        var ret = renewSvc.updateItem(id, updateDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        renewSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
