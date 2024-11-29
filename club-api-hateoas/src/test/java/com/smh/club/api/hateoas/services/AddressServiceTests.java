package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.contracts.assemblers.AddressAssembler;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.models.AddressModel;
import java.util.Optional;
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
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class AddressServiceTests extends ServiceTests {

    @Mock
    private AddressRepo repoMock;

    @Mock
    private MembersRepo membersRepoMock;

    @Mock
    private AddressAssembler assemblerMock;

    @Mock
    private AddressMapper mapperMock;

    @InjectMocks
    AddressServiceImpl svc;

    @Mock
    private Page<AddressEntity> pageMock;

    @Captor
    private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);


    @ParameterizedTest
    @CsvSource({"id, id", "address1, address1", "city, city", "state, state", "zip, zip",
        "address-type, addressType"})
    public void getPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        svc.getPage(pageable);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());

        verify(repoMock).findAll(any(PageRequest.class));
        verify(assemblerMock).toPagedModel(any());
        verifyNoMoreInteractions(repoMock, pageMock);
    }


    @ParameterizedTest
    @ValueSource(strings = {"address2", "member-id"})
    public void getPage_excludes_throw_exception(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getPage(pageable));

        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void gePage_unknown_sortColumn_throws_exception() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getPage(pageable));

        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getAddressListPage_with_descending() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "DESC";
        var sort = "id";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        svc.getPage(pageable);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(sort, order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(assemblerMock).toPagedModel(pageMock);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getPage_returns_pagedModel() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "DESC";
        var sort = "id";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var models = Instancio.ofList(AddressModel.class)
            .size(pageSize)
            .create();

        var pagedModel = createdPageModel(models);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(pagedModel);

        // execute
        var ret = svc.getPage(pageable);

        // verify
        assertEquals(pagedModel, ret);
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getAddress_returns_address() {
        // setup
        int id = 1;
        var entity = Instancio.of(AddressEntity.class).set(field(AddressEntity::getId), id).create();
        var address = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), id)
            .create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(assemblerMock.toModel(any(AddressEntity.class))).thenReturn(address);

        // execute
        var ret = svc.getAddress(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(assemblerMock).toModel(any(AddressEntity.class));
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getAddress_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getAddress(id);

        // verify
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void createAddress_returns_address() {
        // setup
        var model = Instancio.create(AddressModel.class);
        var entity = Instancio.create(AddressEntity.class);

        when(mapperMock.toEntity(model)).thenReturn(entity);
        when(repoMock.save(entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.createAddress(model);

        // verify
        assertEquals(ret, model);
        verify(repoMock).save(entity);
        verify(mapperMock).toEntity(model);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void updateAddress_returns_address() {
        // setup
        var entity = Instancio.create(AddressEntity.class);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var model = Instancio.of(AddressModel.class)
            .set(field(AddressModel::getId), id)
            .set(field(AddressModel::getMemberId), memberId)
            .create();


        when(repoMock.findByIdAndMemberId(id, memberId)).thenReturn(Optional.of(entity));
        when(mapperMock.updateEntity(model, entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.updateAddress(id, model);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findByIdAndMemberId(id, memberId);
        verify(mapperMock).updateEntity(model, entity);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void deleteAddress_deletes_address() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deleteAddress(id);

        //verify
        verify(repoMock).deleteById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getCount_returns_address_count() {
        // setup
        long count = 5;
        when(repoMock.count()).thenReturn(count);

        // execute
        var response = svc.getAddressCount();

        // verify
        assertEquals(count,response);
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

}
