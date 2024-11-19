package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.AddressMapper;
import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.AddressDto;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    @ParameterizedTest
    @CsvSource({"id, id", "address1, address1", "city, city", "state, state",
            "zip, zip", "address-type, addressType"})
    public void getAddressListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(addRepoMock, addMapMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id", "memberId" ,"address2"})
    public void getAddressListPage_excluded_uses_id(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "id";

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(addRepoMock, addMapMock);
    }


    @Test
    public void getAddressListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "id";

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getAddressListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "id";

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }

    @Test
    public void getAddressListPage_with_desc() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "address1";

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(addRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(sort, order.getProperty());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(addRepoMock);
    }


    @Test
    public void getAddressListPage_returns_AddressList() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "address1";
        var total = 200;
        var entityList = Instancio.ofList(AddressEntity.class).size(pageSize).create();
        var dto = Instancio.of(AddressDto.class).create();

        var page = createEntityPage(entityList, pageableMock, total);

        when(addRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(addMapMock.toDto(any(AddressEntity.class))).thenReturn(dto);

        // execute
        var ret = svc.getAddressListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(total, ret.getTotalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(addRepoMock).findAll(any(PageRequest.class));
        verify(addMapMock, times(pageSize)).toDto(any(AddressEntity.class));
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
    public void getAddress_notFound_returns_empty_optional() {
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

        var create = Instancio.of(AddressDto.class)
                .set(field(AddressDto::getMemberId), member.getId())
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
        var update = Instancio.of(AddressDto.class)
                .set(field(AddressDto::getMemberId), id)
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
        assertEquals(count, response);
        verify(addRepoMock).count();
        verifyNoMoreInteractions(addRepoMock, addMapMock, memRepoMock);
    }
}
