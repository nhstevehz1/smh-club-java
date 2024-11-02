package com.smh.club.api.controllers;

import com.smh.club.api.common.controllers.MemberController;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
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
@RequestMapping(value = "members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberControllerImpl implements MemberController {

    private final MemberService memberSvc;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<Member>> getItemListPage(@RequestBody PageParams pageParams ) {
        if (pageParams == null) {
           pageParams = PageParams.getDefault();
        }

        return ResponseEntity.ok(memberSvc.getItemListPage(pageParams));
    }

    @GetMapping("{id}")
    public ResponseEntity<Member> getItem(@PathVariable int id) {
        var ret = memberSvc.getItem(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getCount() {
        return ResponseEntity.ok(memberSvc.getItemCount());
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> createItem(@RequestBody Member member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberSvc.createItem(member));
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> updateItem(@PathVariable int id, @RequestBody Member member) {
        var ret = memberSvc.updateItem(id, member);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        memberSvc.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/detail")
    public ResponseEntity<MemberDetail>  getMemberDetail(int id) {
        var ret = memberSvc.getMemberDetail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
