package com.smh.club.api.controllers;

import com.smh.club.api.Services.IMemberService;
import com.smh.club.api.models.Member;
import com.smh.club.api.models.MemberDetail;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final IMemberService memberSvc;

    public MemberController(IMemberService memberSvc) {
        this.memberSvc = memberSvc;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<Member>> getMemberListPage(@RequestBody PageParams pageParams ) {
        return ResponseEntity.ok(memberSvc.getMemberPage(Optional.of(pageParams)));
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberDetail> getMemberDetail(@PathVariable int id) {
        return ResponseEntity.ok(memberSvc.getMemberDetail(id));
    }

    @GetMapping("count")
    public ResponseEntity<CountResponse> getMemberCount() {
        return ResponseEntity.ok(memberSvc.getMemberCount());
    }

    @PostMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberSvc.createMember(member));
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> updateMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberSvc.updateMember(member));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.ok(null);
    }
}
