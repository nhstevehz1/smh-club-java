package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.data.contracts.services.MemberService;
import com.smh.club.api.data.dto.MemberDto;
import com.smh.club.api.hateoas.assemblers.MemberModelAssembler;
import com.smh.club.api.hateoas.config.PagingConfig;
import com.smh.club.api.hateoas.contracts.controllers.MemberController;
import com.smh.club.api.hateoas.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping(value = "/api/v2/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberControllerImpl implements MemberController {

    private final MemberService memberSvc;
    private final MemberModelAssembler assembler;
    private final PagedResourcesAssembler<MemberDto> pagedAssembler;

    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EntityModel<MemberDto>>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int pageNumber,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int pageSize,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var ret = memberSvc.getMemberListPage(pageNumber, pageSize, sortDir, sort);

        return ResponseEntity.ok(pagedAssembler.toModel(ret, assembler));
    }

    @GetMapping("{id}")
    @Override
    public ResponseEntity<EntityModel<MemberDto>> get(@PathVariable int id) {
        var ret = memberSvc.getMember(id);
        return ret.map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CountResponse> count() {
        return ResponseEntity.ok(CountResponse.of(memberSvc.getMemberCount()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<MemberDto>> create(@RequestBody MemberDto member) {
        var created = memberSvc.createMember(member);
        var model = assembler.toModel(created);
        var selfLink = model.getLink("self").orElseThrow();
        return ResponseEntity.created(selfLink.toUri()).body(model);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<MemberDto>> update(@PathVariable int id, @RequestBody MemberDto member) {
        member.setId(id); // assume the path variable is the source of truth.

        var ret = memberSvc.updateMember(id,  member);

        return ret.map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
