package com.smh.club.api.controllers.v1;

import com.smh.club.api.common.controllers.v1.RenewalController;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.dto.CreateRenewalDto;
import com.smh.club.api.dto.RenewalDto;
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
@RequestMapping(value = "/api/v1/renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalControllerImpl implements RenewalController {
    
    private final RenewalService renewSvc;
    
    @GetMapping
    @Override
    public ResponseEntity<PageResponse<RenewalDto>> page(
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

        return ResponseEntity.ok(renewSvc.getRenewalListPage(pageParams));
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
        return ResponseEntity.ok(renewSvc.getRenewalCount());
    }

    @PostMapping
    @Override
    public ResponseEntity<RenewalDto> create(@RequestBody CreateRenewalDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createRenewal(createDto));
    }

    @PutMapping("{id}")
    @Override
    public ResponseEntity<RenewalDto> update(@PathVariable int id, @RequestBody CreateRenewalDto updateDto) {
        var ret = renewSvc.updateRenewal(id, updateDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        renewSvc.deleteRenewal(id);
        return ResponseEntity.noContent().build();
    }
}
