package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.EmailController;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.models.Email;
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
@RequestMapping(value = "emails", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailControllerImpl implements EmailController {
    
    private final EmailService emailSvc;

    @GetMapping
    public ResponseEntity<PageResponse<Email>> getItemListPage(@RequestBody PageParams pageParams) {
        if (pageParams == null) {
            pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(emailSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<Email> getItem(@PathVariable int id) {
        var ret = emailSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(emailSvc.getItemCount());
    }

    @PostMapping
    public ResponseEntity<Email> createItem(@RequestBody Email email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailSvc.createItem(email));
    }

    @PutMapping("{id}")
    public ResponseEntity<Email> updateItem(@PathVariable int id, @RequestBody Email email) {
        var ret = emailSvc.updateItem(id, email);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        emailSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
