package com.smh.club.api.services;

import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.request.PageParams;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class MemberServiceTests extends ServiceTests {

    @Mock private MembersRepo repoMock;
    @Mock private MemberMapper mapperMock;

    @InjectMocks MemberServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<MemberEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);

    @Test
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(params);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("memberNumber", order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getItemListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,200, Sort.Direction.DESC, "first-name");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(params);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("firstName", order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(params);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("memberNumber", order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, null);
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(params);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("memberNumber", order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "first-name");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getMemberListPage(params));
        verifyNoInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_returns_memberList() {
        // setup
        var size = 20;
        var entityList = Instancio.ofList(MemberEntity.class).size(size).create();
        var dtoList = Instancio.ofList(MemberDto.class).size(size).create();

        var page = createEntityPage(entityList, pageableMock, 200);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(mapperMock.toMemberDtoList(page.getContent())).thenReturn(dtoList);

        // execute
        var pageResponse = svc.getMemberListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(mapperMock).toMemberDtoList(page.getContent());
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_returns_member() {
        // setup
        int id = 1;
        var entity = Instancio.of(MemberEntity.class).set(field(MemberEntity::getId), id).create();
        var member = Instancio.of(MemberDto.class).set(field(MemberDto::getId), id).create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toMemberDto(any(MemberEntity.class))).thenReturn(member);

        // execute
        var ret = svc.getMember(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(mapperMock).toMemberDto(any(MemberEntity.class));
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getMember(id);

        //
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void createMember_returns_member() {
        // setup
        var create = Instancio.create(MemberDto.class);
        var member = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);

        when(repoMock.save(entity)).thenReturn(entity);
        when(mapperMock.toMemberEntity(create)).thenReturn(entity);
        when(mapperMock.toMemberDto(entity)).thenReturn(member);

        // execute
        var ret = svc.createMember(create);

        // verify
        assertNotNull(ret);
        assertEquals(ret, member);
        verify(repoMock).save(entity);
        verify(mapperMock).toMemberEntity(create);
        verify(mapperMock).toMemberDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void updateMember_returns_member() {
        // setup
        int id = 1;
        var update = Instancio.create(MemberDto.class);
        var member = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));

        when(mapperMock.updateMemberEntity(update, entity)).thenReturn(entity);
        when(mapperMock.toMemberDto(entity)).thenReturn(member);

        // execute
        var ret = svc.updateMember(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);

        verify(mapperMock).updateMemberEntity(update, entity);
        verify(mapperMock).toMemberDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void deleteMember_deletes_member() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deleteMember(id);

        //verify
        verify(repoMock).deleteById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getCount_returns_member_count() {
        // setup
        long num = 5;
        when(repoMock.count()).thenReturn(num);

        // execute
        var response = svc.getMemberCount();

        // verify
        assertEquals(num, response.getCount());
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getMemberDetail_returns_memberDetail() {
        // setup
        int id = 1;
        var entity = Instancio.create(MemberEntity.class);
        var memberDetail = Instancio.create(MemberDetailDto.class);

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toMemberDetailDto(entity)).thenReturn(memberDetail);

        var ret = svc.getMemberDetail(id);

        // verify
        assertTrue(ret.isPresent());

        var detail = ret.get();
        assertEquals(memberDetail.getId(), detail.getId());
        assertNotNull(detail.getAddresses());
        assertNotNull(detail.getEmails());
        assertNotNull(detail.getPhones());
        assertNotNull(detail.getRenewals());

        verify(repoMock).findById(id);
        verify(mapperMock).toMemberDetailDto(entity);
        verifyNoMoreInteractions(mapperMock);
    }

    @Test
    public void getMemberDetail_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getMemberDetail(id);

        // verify
        assertFalse(ret.isPresent());
        verify(repoMock).findById(1);
        verifyNoMoreInteractions(mapperMock);
    }

}
