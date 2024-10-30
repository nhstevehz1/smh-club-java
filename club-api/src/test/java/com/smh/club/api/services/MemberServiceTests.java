package com.smh.club.api.services;

import com.smh.club.api.Services.MemberServiceImpl;
import com.smh.club.api.common.mappers.*;
import com.smh.club.api.data.entities.*;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.*;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests extends CrudServiceTestBase<Member, MemberEntity> {

    @Mock private MembersRepo repoMock;
    @Mock private MemberMapper memMapMock;
    @Mock private AddressMapper addMapMock;
    @Mock private EmailMapper emlMapMock;
    @Mock private PhoneMapper phnMapMock;
    @Mock private RenewalMapper renMapMock;

    @InjectMocks MemberServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<MemberEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_nonDefaultPageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "first-name");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsUnknownSortColumn() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsNullSortColumn() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, null);
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsNullSortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "first-name");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> this.svc.getItemListPage(params));
        verifyNoInteractions(repoMock);
    }

    @Test
    public void getItemListPage_returnsMemberList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(repoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(memMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = this.svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(memMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void getItem_returnsMember() {
        // setup
        int id = 1;
        var optional = Optional.of(createEntity(id));
        when(repoMock.findById(id)).thenReturn(optional);
        when(memMapMock.toDataObject(any(MemberEntity.class))).thenReturn(createDataObject(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertNotNull(member);
        verify(repoMock).findById(id);
        verify(memMapMock).toDataObject(any(MemberEntity.class));
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void getItem_notFound_ThrowsException() {
        // setup
        int id = 1;
        Optional<MemberEntity> optional = Optional.empty();
        when(repoMock.findById(id)).thenReturn(optional);

        // execute and verify
        assertThrows(NoSuchElementException.class, () -> svc.getItem(id));
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void createItem_returnsMember() {
        // setup
        var m1 = createDataObject(1);
        var entity = createEntity(1);
        when(repoMock.save(entity)).thenReturn(entity);
        when(memMapMock.toEntity(m1)).thenReturn(entity);
        when(memMapMock.toDataObject(entity)).thenReturn(m1);

        // execute
        var member = svc.createItem(m1);

        // verify
        assertNotNull(member);
        assertEquals(m1, member);
        verify(repoMock).save(entity);
        verify(memMapMock).toEntity(m1);
        verify(memMapMock).toDataObject(entity);
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void updateItem_returnsMember() {
        // setup
        int id = 1;
        var entity = createEntity(1);
        var m1 = createDataObject(1);
        var optional = Optional.of(entity);

        when(repoMock.findById(id)).thenReturn(optional);
        when(repoMock.save(entity)).thenReturn(entity);

        doNothing().when(memMapMock).updateEntity(m1, entity);
        when(memMapMock.toDataObject(entity)).thenReturn(m1);

        // execute
        var member = svc.updateItem(id, m1);

        // verify
        assertNotNull(member);
        verify(repoMock).findById(id);
        verify(repoMock).save(entity);

        verify(memMapMock).updateEntity(m1, entity);
        verify(memMapMock).toDataObject(entity);
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void deleteItem_deletesMember() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(repoMock).deleteById(id);
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void getCount() {
        // setup
        long num = 5;
        when(repoMock.count()).thenReturn(num);

        // execute
        var response = svc.getItemCount();

        // verify
        assertEquals(num, response.getCount());
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, memMapMock);
    }

    @Test
    public void getMemberDetail() {
        // setup
        int id = 1;
        var entity = createEntity(1);
        entity.setAddresses(List.of(AddressEntity.builder().build()));
        entity.setEmails(List.of(EmailEntity.builder().build()));
        entity.setPhones(List.of(PhoneEntity.builder().build()));
        entity.setRenewals(List.of(RenewalEntity.builder().build()));
        var optional = Optional.of(entity);
        when(repoMock.findById(id)).thenReturn(optional);

        when(memMapMock.toMemberDetail(entity)).thenReturn(MemberDetail.builder().build());
        when(addMapMock.toDataObjectList(entity.getAddresses())).thenReturn(List.of(Address.builder().build()));
        when(emlMapMock.toDataObjectList(entity.getEmails())).thenReturn(List.of(Email.builder().build()));
        when(phnMapMock.toDataObjectList(entity.getPhones())).thenReturn(List.of(Phone.builder().build()));
        when(renMapMock.toDataObjectList(entity.getRenewals())).thenReturn(List.of(Renewal.builder().build()));

        var detail = svc.getMemberDetail(id);

        // verify
        assertNotNull(detail.getAddresses());
        assertEquals(1, detail.getAddresses().size());
        assertNotNull(detail.getEmails());
        assertEquals(1, detail.getPhones().size());
        assertNotNull(detail.getPhones());
        assertEquals(1, detail.getPhones().size());
        assertNotNull(detail.getRenewals());
        assertEquals(1, detail.getRenewals().size());

        verify(repoMock).findById(id);

        verify(memMapMock).toMemberDetail(entity);
        verify(addMapMock).toDataObjectList(entity.getAddresses());
        verify(emlMapMock).toDataObjectList(entity.getEmails());
        verify(phnMapMock).toDataObjectList(entity.getPhones());
        verify(renMapMock).toDataObjectList(entity.getRenewals());

        verifyNoMoreInteractions(renMapMock, memMapMock, addMapMock, phnMapMock, renMapMock);
    }

    @Test
    public void getMemberDetail_NotFoundThrowsException() {
        // setup
        int id = 1;
        Optional<MemberEntity> optional = Optional.empty();
        when(repoMock.findById(id)).thenReturn(optional);

        // execute and verify
        assertThrows(NoSuchElementException.class, () -> svc.getMemberDetail(id));
        verifyNoMoreInteractions(renMapMock, memMapMock, addMapMock, phnMapMock, renMapMock);
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

    protected Member createDataObject(int flag) {
        var now = LocalDate.now();

        return Member.builder()
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
