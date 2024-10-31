package com.smh.club.api.services;

import com.smh.club.api.Services.PhoneServiceImpl;
import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.entities.PhoneEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.PhoneRepo;
import com.smh.club.api.models.Phone;
import com.smh.club.api.models.PhoneType;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceTests extends CrudServiceTestBase<Phone, PhoneEntity> {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private PhoneRepo phnRepoMock;
    @Mock private PhoneMapper phnMapMock;

    @InjectMocks private PhoneServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<PhoneEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_nonDefaultPageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "phone-num");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("phoneNum", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsUnknownSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsNullSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsNullSortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "phoneNum");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> this.svc.getItemListPage(params));
        verifyNoInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_returnsPhoneList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(phnMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = this.svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verify(phnMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_returnsPhone() {
        // setup
        int id = 1;
        var optional = Optional.of(createEntity(id));
        when(phnRepoMock.findById(id)).thenReturn(optional);
        when(phnMapMock.toDataObject(any(PhoneEntity.class))).thenReturn(createDataObject(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertNotNull(member);
        verify(phnRepoMock).findById(id);
        verify(phnMapMock).toDataObject(any(PhoneEntity.class));
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_ThrowsException() {
        // setup
        int id = 1;
        Optional<PhoneEntity> optional = Optional.empty();
        when(phnRepoMock.findById(id)).thenReturn(optional);

        // execute and verify
        assertThrows(NoSuchElementException.class, () -> svc.getItem(id));
        verify(phnRepoMock).findById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void createItem_returnsPhone() {
        // setup
        var memberId = 10;
        var m1 = MemberEntity.builder().id(memberId).build();
        var optional = Optional.of(m1);
        when(memRepoMock.findById(memberId)).thenReturn(optional);

        var a1 = createDataObject(1);
        a1.setMemberId(memberId);
        var entity = createEntity(1);
        when(phnRepoMock.save(entity)).thenReturn(entity);
        when(phnMapMock.toEntity(a1)).thenReturn(entity);
        when(phnMapMock.toDataObject(entity)).thenReturn(a1);

        // execute
        var address = svc.createItem(a1);

        // verify
        assertNotNull(address);
        assertEquals(a1, address);
        verify(memRepoMock).findById(memberId);
        verify(phnRepoMock).save(entity);
        verify(phnMapMock).toEntity(a1);
        verify(phnMapMock).toDataObject(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returnsPhone() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var e1 = createDataObject(id);
        var optional = Optional.of(entity);

        when(phnRepoMock.findById(id)).thenReturn(optional);
        when(phnRepoMock.save(entity)).thenReturn(entity);

        doNothing().when(phnMapMock).updateEntity(e1, entity);
        when(phnMapMock.toDataObject(entity)).thenReturn(e1);

        // execute
        var member = svc.updateItem(id, e1);

        // verify
        assertNotNull(member);
        verify(phnRepoMock).findById(id);
        verify(phnRepoMock).save(entity);

        verify(phnMapMock).updateEntity(e1, entity);
        verify(phnMapMock).toDataObject(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletesPhone() {
        // setup
        int id = 1;
        doNothing().when(phnRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(phnRepoMock).deleteById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }
    
    @Override
    protected PhoneEntity createEntity(int flag) {
        return PhoneEntity.builder()
                .id(flag)
                .phoneNum("phoneNum")
                .phoneType(PhoneType.Home)
                .build();
    }

    @Override
    protected Phone createDataObject(int flag) {
        return Phone.builder()
                .id(flag)
                .phoneNum("phoneNum")
                .phoneType(PhoneType.Home)
                .build();
    }
}
