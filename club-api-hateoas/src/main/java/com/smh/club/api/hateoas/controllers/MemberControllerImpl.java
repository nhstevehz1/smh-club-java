package com.smh.club.api.hateoas.controllers;

import com.smh.club.api.common.assemblers.MemberModelAssembler;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.hateoas.contracts.controllers.MemberController;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.request.PagingConfig;
import com.smh.club.api.response.CountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    /*private final MemberService memberSvc;
    private final MemberModelAssembler assembler;
    private final PagedResourcesAssembler<MemberDto> pagedAssembler;

    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EntityModel<MemberDto>>> page(
            @RequestParam(value = PagingConfig.PAGE_NAME,
                    defaultValue = "${request.paging.page}") int page,
            @RequestParam(value = PagingConfig.SIZE_NAME,
                    defaultValue = "${request.paging.size}") int size,
            @RequestParam(value = PagingConfig.DIRECTION_NAME,
                    defaultValue = "${request.paging.direction}") String sortDir,
            @RequestParam(value = PagingConfig.SORT_NAME,
                    defaultValue = "") String sort) {

        var pageParams = PageParams.builder()
                .pageNumber(page)
                .pageSize(size)
                .sortDirection(Sort.Direction.fromString(sortDir))
                .sortColumn(sort)
                .build();

        var ret = memberSvc.getMemberListPageV2(pageParams);

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
        return ResponseEntity.ok(memberSvc.getMemberCount());
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<MemberDto>> create(@RequestBody MemberDto member) {
        var created = memberSvc.createMember(member);
        var model = assembler.toModel(created);
        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                MemberControllerImpl.class).get(created.getId())).toUri()).body(model);
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

    @Override
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        memberSvc.deleteMember(id);
        return ResponseEntity.noContent().build();
    }*/
}
