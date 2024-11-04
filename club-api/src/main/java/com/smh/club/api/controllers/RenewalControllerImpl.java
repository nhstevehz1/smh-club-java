package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.RenewalController;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "renewals", produces = MediaType.APPLICATION_JSON_VALUE)
public class RenewalControllerImpl implements RenewalController {
    
    private final RenewalService renewSvc;
    
    @GetMapping
    public ResponseEntity<PageResponse<RenewalDto>> getItemListPage(@RequestBody PageParams pageParams) {
        if (pageParams == null) {
            pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(renewSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<RenewalDto> getItem(@PathVariable int id) {
        var ret = renewSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(renewSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<RenewalDto> createItem(@RequestBody RenewalDto renewalDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createItem(renewalDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<RenewalDto> updateItem(@PathVariable int id, @RequestBody RenewalDto renewalDto) {
        var ret = renewSvc.updateItem(id, renewalDto);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        renewSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
