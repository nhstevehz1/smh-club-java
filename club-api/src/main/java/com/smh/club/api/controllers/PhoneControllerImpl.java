package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.PhoneController;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.models.Phone;
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
@RequestMapping(value = "phones", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneControllerImpl implements PhoneController {
    
    private final PhoneService phoneSvc;

    @GetMapping
    public ResponseEntity<PageResponse<Phone>> getItemListPage(@RequestBody PageParams pageParams) {
        if (pageParams == null) {
            pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(phoneSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<Phone> getItem(@PathVariable int id) {
        var ret = phoneSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(phoneSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<Phone> createItem(@RequestBody Phone phone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneSvc.createItem(phone));
    }

    @PutMapping("{id}")
    public ResponseEntity<Phone> updateItem(@PathVariable int id, @RequestBody Phone phone) {
        var ret = phoneSvc.updateItem(id, phone);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        phoneSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
