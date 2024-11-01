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

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_with_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "address1");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

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
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "address1");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getItemListPage(params));
        verifyNoInteractions(addRepoMock);
    }

    @Test
    public void getItemListPage_returnsAddressList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(addMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verify(addMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_address() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var address = createDataObject(id);
        when(addRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(addMapMock.toDataObject(entity)).thenReturn(address);

        // execute
        var ret = svc.getItem(id);

        // verify
        assertTrue(ret.isPresent());
        verify(addRepoMock).findById(id);
        verify(addMapMock).toDataObject(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(addRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getItem(id);

        // execute and verify
        assertFalse(ret.isPresent());
        verify(addRepoMock).findById(id);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_address() {
        // setup
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var address = createDataObject(1);
        address.setMemberId(memberId);

        var entity = createEntity(1);
        when(addRepoMock.save(entity)).thenReturn(entity);
        when(addMapMock.toEntity(address)).thenReturn(entity);
        when(addMapMock.toDataObject(entity)).thenReturn(address);

        // execute
        var ret = svc.createItem(address);

        // verify
        assertNotNull(address);
        assertEquals(ret, address);
        verify(memRepoMock).getReferenceById(memberId);
        verify(addRepoMock).save(entity);
        verify(addMapMock).toEntity(address);
        verify(addMapMock).toDataObject(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_address() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var address= createDataObject(id);

        when(addRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(addMapMock.updateEntity(address, entity)).thenReturn(entity);
        when(addMapMock.toDataObject(entity)).thenReturn(address);

        // execute
        var ret = svc.updateItem(id, address);

        // verify
        assertTrue(ret.isPresent());
        verify(addRepoMock).findByIdAndMemberId(id, id);

        verify(addMapMock).updateEntity(address, entity);
        verify(addMapMock).toDataObject(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_address() {
        // setup
        int id = 1;
        doNothing().when(addRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(addRepoMock).deleteById(id);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getCount_returns_address_count() {
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
    protected AddressEntity createEntity(int flag) {
        return AddressEntity.builder()
                .id(flag)
                .address1("e_address1_" + flag)
                .address2("e_address2_" + flag)
                .city("e_city_" + flag)
                .state("e_state_" + flag)
                .zip("e_zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }

    @Override
    protected Address createDataObject(int flag) {
        return Address.builder()
                .id(flag)
                .memberId(flag)
                .address1("a_address1_" + flag)
                .address2("address2_" + flag)
                .city("city_" + flag)
                .state("state_" + flag)
                .zip("zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }
}
