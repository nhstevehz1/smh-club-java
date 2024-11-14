package com.smh.club.api.controllers.v1;

import com.smh.club.api.common.controllers.v1.MemberController;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.dto.CreateMemberDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberControllerImpl implements MemberController {

    private final MemberService memberSvc;

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<MemberDto>> page(
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

        return ResponseEntity.ok(memberSvc.getMemberListPage(pageParams));
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<MemberDto> get(@PathVariable int id) {
        var ret = memberSvc.getMember(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("count")
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(memberSvc.getMemberCount());
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<MemberDto> create(@RequestBody CreateMemberDto member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberSvc.createMember(member));
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<MemberDto> update(@PathVariable int id, @RequestBody CreateMemberDto member) {
        var ret = memberSvc.updateMember(id,  member);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/detail")
    @Override
    public ResponseEntity<MemberDetailDto> detail(@PathVariable int id) {
        var ret = memberSvc.getMemberDetail(id);
        return ret.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
