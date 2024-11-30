package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.entities.PhoneEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.PhoneRepo;
import com.smh.club.api.hateoas.contracts.assemblers.PhoneAssembler;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.models.PhoneModel;
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
public class PhoneServiceTests extends ServiceTests {

    @Mock
    private PhoneRepo repoMock;

    @Mock
    private MembersRepo membersRepoMock;

    @Mock
    private PhoneAssembler assemblerMock;

    @Mock
    private PhoneMapper mapperMock;

    @InjectMocks
    PhoneServiceImpl svc;

    @Mock
    private Page<PhoneEntity> pageMock;

    @Captor
    private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);


    @ParameterizedTest
    @CsvSource({"id, id", "phone-number, phoneNumber", "phone-type, phoneType"})
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
    @ValueSource(strings = {"member-id"})
    public void getPage_excludes_throws_exception(String sort) {
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
    public void getPage_unknown_sortColumn_throws_exception() {
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
    public void getPage_with_descending() {
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

        var models = Instancio.ofList(PhoneModel.class)
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
    public void getItem_returns_phone() {
        // setup
        int id = 1;
        var entity = Instancio.of(PhoneEntity.class).set(field(PhoneEntity::getId), id).create();
        var phone = Instancio.of(PhoneModel.class)
            .set(field(PhoneModel::getId), id)
            .set(field(PhoneModel::getMemberId), id)
            .create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(assemblerMock.toModel(any(PhoneEntity.class))).thenReturn(phone);

        // execute
        var ret = svc.getPhone(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(assemblerMock).toModel(any(PhoneEntity.class));
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getPhone(id);

        // verify
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void createPhone_returns_phone() {
        // setup
        var model = Instancio.create(PhoneModel.class);
        var entity = Instancio.create(PhoneEntity.class);

        when(mapperMock.toEntity(model)).thenReturn(entity);
        when(repoMock.save(entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.createPhone(model);

        // verify
        assertEquals(ret, model);
        verify(repoMock).save(entity);
        verify(mapperMock).toEntity(model);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void updatePhone_returns_phone() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);
        var id = entity.getId();
        var memberId = entity.getMember().getId();
        var model = Instancio.of(PhoneModel.class)
            .set(field(PhoneModel::getId), id)
            .set(field(PhoneModel::getMemberId), memberId)
            .create();


        when(repoMock.findByIdAndMemberId(id, memberId)).thenReturn(Optional.of(entity));
        when(mapperMock.updateEntity(model, entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.updatePhone(id, model);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findByIdAndMemberId(id, memberId);
        verify(mapperMock).updateEntity(model, entity);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void deletePhone_deletes_phone() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deletePhone(id);

        //verify
        verify(repoMock).deleteById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getCount_returns_phone_count() {
        // setup
        long count = 5;
        when(repoMock.count()).thenReturn(count);

        // execute
        var response = svc.getPhoneCount();

        // verify
        assertEquals(count,response);
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, mapperMock);
    }
}
