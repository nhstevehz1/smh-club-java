package com.smh.club.api.services;

import com.smh.club.api.Services.AddressServiceImpl;
import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.repos.AddressRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.Address;
import com.smh.club.api.models.AddressType;
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
public class AddressServiceTests extends CrudServiceTestBase<Address, AddressEntity> {
    @Mock private MembersRepo memRepoMock;
    @Mock private AddressRepo addRepoMock;
    @Mock private AddressMapper addMapMock;

    @InjectMocks private AddressServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<AddressEntity> pageMock;

    @Captor
    private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_nonDefaultPageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "address1");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("address1", order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsUnknownSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsNullSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_PageParamsNullSortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "address1");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> this.svc.getItemListPage(params));
        verifyNoInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_returnsAddressList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(addMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = this.svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verify(addMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_returnsAddress() {
        // setup
        int id = 1;
        var optional = Optional.of(createEntity(id));
        when(addRepoMock.findById(id)).thenReturn(optional);
        when(addMapMock.toDataObject(any(AddressEntity.class))).thenReturn(createDataObject(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertNotNull(member);
        verify(addRepoMock).findById(id);
        verify(addMapMock).toDataObject(any(AddressEntity.class));
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_ThrowsException() {
        // setup
        int id = 1;
        Optional<AddressEntity> optional = Optional.empty();
        when(addRepoMock.findById(id)).thenReturn(optional);

        // execute and verify
        assertThrows(NoSuchElementException.class, () -> svc.getItem(id));
        verify(addRepoMock).findById(id);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void createItem_returnsMember() {
        // setup
        var memberId = 10;
        var m1 = MemberEntity.builder().id(memberId).build();
        var optional = Optional.of(m1);
        when(memRepoMock.findById(memberId)).thenReturn(optional);

        var a1 = createDataObject(1);
        a1.setMemberId(memberId);
        var entity = createEntity(1);
        when(addRepoMock.save(entity)).thenReturn(entity);
        when(addMapMock.toEntity(a1)).thenReturn(entity);
        when(addMapMock.toDataObject(entity)).thenReturn(a1);

        // execute
        var address = svc.createItem(a1);

        // verify
        assertNotNull(address);
        assertEquals(a1, address);
        verify(memRepoMock).findById(memberId);
        verify(addRepoMock).save(entity);
        verify(addMapMock).toEntity(a1);
        verify(addMapMock).toDataObject(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returnsMember() {
        // setup
        int id = 1;
        var entity = createEntity(1);
        var m1 = createDataObject(1);
        var optional = Optional.of(entity);

        when(addRepoMock.findById(id)).thenReturn(optional);
        when(addRepoMock.save(entity)).thenReturn(entity);

        doNothing().when(addMapMock).updateEntity(m1, entity);
        when(addMapMock.toDataObject(entity)).thenReturn(m1);

        // execute
        var member = svc.updateItem(id, m1);

        // verify
        assertNotNull(member);
        verify(addRepoMock).findById(id);
        verify(addRepoMock).save(entity);

        verify(addMapMock).updateEntity(m1, entity);
        verify(addMapMock).toDataObject(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletesMember() {
        // setup
        int id = 1;
        doNothing().when(addRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(addRepoMock).deleteById(id);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }
    
    @Override
    protected AddressEntity createEntity(int flag) {
        return AddressEntity.builder()
                .id(flag)
                .address1("address1")
                .address2("address2")
                .city("city")
                .state("state")
                .zip("zip")
                .addressType(AddressType.Home)
                .build();
    }

    @Test
    public void getCount() {
        // setup
        long num = 5;
        when(addRepoMock.count()).thenReturn(num);

        // execute
        var response = svc.getItemCount();

        // verify
        assertEquals(num, response.getCount());
        verify(addRepoMock).count();
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }
    
    @Override
    protected Address createDataObject(int flag) {
        return Address.builder()
                .id(flag)
                .memberId(flag)
                .address1("address1")
                .address2("address2")
                .city("city")
                .state("state")
                .zip("zip")
                .addressType(AddressType.Home)
                .build();
    }
}
