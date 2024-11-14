package com.smh.club.api.services;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.CreateAddressDto;
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
public class AddressServiceTests extends ServiceTests {

    @Mock private MembersRepo memRepoMock;
    @Mock private AddressRepo addRepoMock;
    @Mock private AddressMapper addMapMock;

    @InjectMocks private AddressServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<AddressEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void getAddressListPage_with_default_pageParams() {
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
        var params = createPageParam(5, 100, Sort.Direction.DESC, "unknownColumn");
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
    public void getAddressListPage_returns_AddressList() {
        // setup
        var size = 10;
        var entityList = Instancio.ofList(AddressEntity.class).size(size).create();
        var dtoList = Instancio.ofList(AddressDto.class).size(size).create();

        var page = createEntityPage(entityList, pageableMock, 200);

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(addMapMock.toDtoList(page.getContent())).thenReturn(dtoList);

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
        var entity = Instancio.of(AddressEntity.class).set(field(AddressEntity::getId), id).create();
        var address = Instancio.of(AddressDto.class).set(field(AddressDto::getId), id).create();

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
        var member = Instancio.create(MemberEntity.class);
        when(memRepoMock.getReferenceById(member.getId())).thenReturn(member);

        var create = Instancio.of(CreateAddressDto.class)
                .set(field(CreateAddressDto::getMemberId), member.getId())
                .create();
        var address = Instancio.of(AddressDto.class)
                .set(field(AddressDto::getMemberId), member.getId())
                .create();

        var entity = Instancio.of(AddressEntity.class)
                .set(field(AddressEntity::getMember), member)
                .create();

        when(addRepoMock.save(entity)).thenReturn(entity);
        when(addMapMock.toEntity(create)).thenReturn(entity);
        when(addMapMock.toDto(entity)).thenReturn(address);

        // execute
        var ret = svc.createAddress(create);

        // verify
        assertNotNull(ret);
        assertEquals(address, ret);
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
        var entity = Instancio.create(AddressEntity.class);
        var update = Instancio.of(CreateAddressDto.class)
                .set(field(CreateAddressDto::getMemberId), id)
                .create();
        var address = Instancio.create(AddressDto.class);

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
        long count = 5;
        when(addRepoMock.count()).thenReturn(count);

        // execute
        var response = svc.getAddressCount();

        // verify
        assertEquals(count, response.getCount());
        verify(addRepoMock).count();
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }
}
