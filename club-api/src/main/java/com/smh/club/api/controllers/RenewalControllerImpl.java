package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.RenewalController;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.models.Renewal;
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
    public ResponseEntity<PageResponse<Renewal>> getItemListPage(@RequestBody PageParams pageParams) {
        if (pageParams == null) {
            pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(renewSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<Renewal> getItem(@PathVariable int id) {
        var ret = renewSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(renewSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<Renewal> createItem(@RequestBody Renewal renewal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(renewSvc.createItem(renewal));
    }

    @PutMapping("{id}")
    public ResponseEntity<Renewal> updateItem(@PathVariable int id, @RequestBody Renewal renewal) {
        var ret = renewSvc.updateItem(id, renewal);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        renewSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
