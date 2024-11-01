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
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "phone-num");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_unknown_sortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "phoneNum");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getItemListPage(params));
        verifyNoInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_returns_phoneList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(phnMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verify(phnMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_phone() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        when(phnRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(phnMapMock.toDataObject(entity)).thenReturn(createDataObject(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertTrue(member.isPresent());
        verify(phnRepoMock).findById(id);
        verify(phnMapMock).toDataObject(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(phnRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getItem(id);

        // and verify
        assertFalse(ret.isPresent());
        verify(phnRepoMock).findById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_phone() {
        // setup
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var address = createDataObject(1);
        address.setMemberId(memberId);

        var entity = createEntity(1);
        when(phnRepoMock.save(entity)).thenReturn(entity);
        when(phnMapMock.toEntity(address)).thenReturn(entity);
        when(phnMapMock.toDataObject(entity)).thenReturn(address);

        // execute
        var ret = svc.createItem(address);

        // verify
        assertNotNull(address);
        assertEquals(address, ret);
        verify(memRepoMock).getReferenceById(memberId);
        verify(phnRepoMock).save(entity);
        verify(phnMapMock).toEntity(address);
        verify(phnMapMock).toDataObject(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_phone() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var email = createDataObject(id);

        when(phnRepoMock.findById(id)).thenReturn(Optional.of(entity));

        when(phnMapMock.updateEntity(email, entity)).thenReturn(entity);
        when(phnMapMock.toDataObject(entity)).thenReturn(email);

        // execute
        var ret = svc.updateItem(id, email);

        // verify
        assertTrue(ret.isPresent());
        verify(phnRepoMock).findById(id);

        verify(phnMapMock).updateEntity(email, entity);
        verify(phnMapMock).toDataObject(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_phone() {
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
