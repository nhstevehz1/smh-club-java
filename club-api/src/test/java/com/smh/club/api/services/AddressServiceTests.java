package com.smh.club.api.services;

import com.smh.club.api.Services.AddressServiceImpl;
import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
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

import static com.smh.club.api.helpers.datacreators.AddressCreators.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTests extends ServiceTests {
    @Mock private MembersRepo memRepoMock;
    @Mock private AddressRepo addRepoMock;
    @Mock private AddressMapper addMapMock;

    @InjectMocks private AddressServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<AddressEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getAddressListPage_with_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(params);

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
    public void getAddressListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "address1");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(params);

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
    public void getAddressListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(params);

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
    public void getAddressListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(params);

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
    public void getAddressListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "address1");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getAddressListPage(params));
        verifyNoInteractions(addRepoMock);
    }

    @Test
    public void getAddressListPage_returnsAddressList() {
        // setup
        var entityList = genAddressEntityList(10);
        var page = createEntityPage(entityList, pageableMock, 200);

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(addMapMock.toDtoList(page.getContent())).thenReturn(genAddressDtoList(10));

        // execute
        var pageResponse = svc.getAddressListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verify(addMapMock).toDtoList(page.getContent());
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_address() {
        // setup
        int id = 1;
        var entity = genAddressEntity(id);
        var address = genAddressDto(id);
        when(addRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(addMapMock.toDto(entity)).thenReturn(address);

        // execute
        var ret = svc.getAddress(id);

        // verify
        assertTrue(ret.isPresent());
        verify(addRepoMock).findById(id);
        verify(addMapMock).toDto(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(addRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getAddress(id);

        // execute and verify
        assertFalse(ret.isPresent());
        verify(addRepoMock).findById(id);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void createAddress_returns_addressDto() {
        // setup
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var create = genCreateAddressDto(memberId);
        var address = genAddressDto(1);
        address.setMemberId(memberId);

        var entity = genAddressEntity(1);
        when(addRepoMock.save(entity)).thenReturn(entity);
        when(addMapMock.toEntity(create)).thenReturn(entity);
        when(addMapMock.toDto(entity)).thenReturn(address);

        // execute
        var ret = svc.createAddress(create);

        // verify
        assertNotNull(address);
        assertEquals(ret, address);
        verify(memRepoMock).getReferenceById(member.getId());
        verify(addRepoMock).save(entity);
        verify(addMapMock).toEntity(create);
        verify(addMapMock).toDto(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void updateAddress_returns_address() {
        // setup
        int id = 1;
        var entity = genAddressEntity(id);
        var update = genUpdateAddressDto(id);
        var address = genAddressDto(id);

        when(addRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(addMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(addMapMock.toDto(entity)).thenReturn(address);

        // execute
        var ret = svc.updateAddress(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(addRepoMock).findByIdAndMemberId(id, id);

        verify(addMapMock).updateEntity(update, entity);
        verify(addMapMock).toDto(entity);
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_address() {
        // setup
        int id = 1;
        doNothing().when(addRepoMock).deleteById(id);

        // execute
        svc.deleteAddress(id);

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
        var response = svc.getAddressCount();

        // verify
        assertEquals(num, response.getCount());
        verify(addRepoMock).count();
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }
}
