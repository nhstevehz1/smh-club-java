package com.smh.club.api.services;

import com.smh.club.api.Services.MemberServiceImpl;
import com.smh.club.api.common.mappers.MemberMapper;
import com.smh.club.api.domain.entities.*;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.dto.MemberMinimumDto;
import com.smh.club.api.request.PageParams;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests extends CrudServiceTestBase<MemberMinimumDto, MemberEntity> {

    @Mock private MembersRepo repoMock;
    @Mock private MemberMapper mapperMock;

    @InjectMocks MemberServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<MemberEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
        var params = createPageParam(5,100, Sort.Direction.DESC, "first-name");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, null);
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "first-name");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getItemListPage(params));
        verifyNoInteractions(repoMock);
    }

    @Test
    public void getItemListPage_returns_memberList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(mapperMock.toDtoList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(mapperMock).toDtoList(page.getContent());
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_returns_member() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toDto(any(MemberEntity.class))).thenReturn(createDataObject(id));

        // execute
        var ret = svc.getItem(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(mapperMock).toDto(any(MemberEntity.class));
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getItem(id);

        //
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void createItem_returns_member() {
        // setup
        var m1 = createDataObject(1);
        var entity = createEntity(1);
        when(repoMock.save(entity)).thenReturn(entity);
        when(mapperMock.toEntity(m1)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(m1);

        // execute
        var member = svc.createItem(m1);

        // verify
        assertNotNull(member);
        assertEquals(m1, member);
        verify(repoMock).save(entity);
        verify(mapperMock).toEntity(m1);
        verify(mapperMock).toDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void updateItem_returns_member() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var member = createDataObject(id);

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));

        when(mapperMock.updateEntity(member, entity)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(member);

        // execute
        var ret = svc.updateItem(id, member);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);

        verify(mapperMock).updateEntity(member, entity);
        verify(mapperMock).toDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void deleteItem_deletes_member() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

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
        var response = svc.getItemCount();

        // verify
        assertEquals(num, response.getCount());
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getMemberDetail_returns_memberDetail() {
        // setup
        int id = 1;
        var entity = createEntity(1);
        entity.setAddresses(List.of(AddressEntity.builder().build()));
        entity.setEmails(List.of(EmailEntity.builder().build()));
        entity.setPhones(List.of(PhoneEntity.builder().build()));
        entity.setRenewals(List.of(RenewalEntity.builder().build()));

        var memberDto = createMemberDto();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toMemberDto(entity)).thenReturn(memberDto);

        var ret = svc.getMemberDetail(id);

        // verify
        assertTrue(ret.isPresent());

        var detail = ret.get();
        assertNotNull(detail.getAddresses());
        assertNotNull(detail.getEmails());
        assertNotNull(detail.getPhones());
        assertNotNull(detail.getRenewals());

        verify(repoMock).findById(id);
        verify(mapperMock).toMemberDto(entity);
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

    private MemberDto createMemberDto() {
        var now = LocalDate.now();
        return MemberDto.builder()
                .id(1)
                .memberNumber(1)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .suffix("jr")
                .birthDate(now.minusYears(1))
                .joinedDate(now)
                .build();
    }

    protected MemberEntity createEntity(int flag) {
        var now = LocalDate.now();

        return MemberEntity.builder()
                .id(flag)
                .memberNumber(flag)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .suffix("jr")
                .birthDate(now.minusYears(flag))
                .joinedDate(now)
                .build();
    }

    protected MemberMinimumDto createDataObject(int flag) {
        var now = LocalDate.now();

        return MemberMinimumDto.builder()
                .id(flag)
                .memberNumber(flag)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .suffix("jr")
                .birthDate(now.minusYears(flag))
                .joinedDate(now)
                .build();
    }

}
