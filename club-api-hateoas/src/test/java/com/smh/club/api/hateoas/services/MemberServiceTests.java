package com.smh.club.api.hateoas.services;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.hateoas.assemblers.MemberAssemblerImpl;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.models.MemberModel;
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
import org.springframework.hateoas.PagedModel;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class MemberServiceTests extends ServiceTests {

    @Mock
    private MembersRepo repoMock;

    @Mock
    private MemberAssemblerImpl assemblerMock;

    @Mock
    private MemberMapper mapperMock;

    @InjectMocks
    MemberServiceImpl svc;

    @Mock private Page<MemberEntity> pageMock;

    @Captor
    private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);

    @ParameterizedTest
    @CsvSource({"id, id", "member-number, memberNumber", "first-name, firstName", "last-name, lastName",
                "birth-date, birthDate", "joined-date, joinedDate"})
    public void getMemberListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        var ret = svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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
    @ValueSource(strings = {"middle-name", "middleName", "suffix"})
    public void getMemberListPage_excludes_use_memberId(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());


        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getMemberListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);


        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(repoMock).findAll(any(PageRequest.class));
        verify(assemblerMock).toPagedModel(pageMock);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getMemberListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(repoMock).findAll(any(PageRequest.class));
        verify(assemblerMock).toPagedModel(pageMock);
        verify(assemblerMock).toPagedModel(pageMock);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getMemberListPage_with_descending() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(PagedModel.empty());

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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
    public void getMemberListPage_returns_pagedModel() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";
        var total = 200;

        var models = Instancio.ofList(MemberModel.class)
            .size(pageSize)
            .create();

        var pagedModel = createdPageModel(models);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(assemblerMock.toPagedModel(pageMock)).thenReturn(pagedModel);

        // execute
        var ret = svc.getMemberListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(pagedModel, ret);
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getItem_returns_member() {
        // setup
        int id = 1;
        var entity = Instancio.of(MemberEntity.class).set(field(MemberEntity::getId), id).create();
        var member = Instancio.of(MemberModel.class).set(field(MemberModel::getId), id).create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(assemblerMock.toModel(any(MemberEntity.class))).thenReturn(member);

        // execute
        var ret = svc.getMember(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(assemblerMock).toModel(any(MemberEntity.class));
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getMember(id);

        // verify
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, assemblerMock);
    }

    @Test
    public void createMember_returns_member() {
        // setup
        var model = Instancio.create(MemberModel.class);
        var entity = Instancio.create(MemberEntity.class);

        when(mapperMock.toEntity(model)).thenReturn(entity);
        when(repoMock.save(entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.createMember(model);

        // verify
        assertEquals(ret, model);
        verify(repoMock).save(entity);
        verify(mapperMock).toEntity(model);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void updateMember_returns_member() {
        // setup
        var model = Instancio.create(MemberModel.class);
        var id = model.getId();
        var entity = Instancio.of(MemberEntity.class).set(field(MemberEntity::getId), id).create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.updateEntity(model, entity)).thenReturn(entity);
        when(assemblerMock.toModel(entity)).thenReturn(model);

        // execute
        var ret = svc.updateMember(id, model);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(mapperMock).updateEntity(model, entity);
        verify(assemblerMock).toModel(entity);
        verifyNoMoreInteractions(repoMock, assemblerMock, mapperMock);
    }

    @Test
    public void deleteMember_deletes_member() {
        // setup
        int id = 1;
        doNothing().when(repoMock).deleteById(id);

        // execute
        svc.deleteMember(id);

        //verify
        verify(repoMock).deleteById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getCount_returns_member_count() {
        // setup
        long count = 5;
        when(repoMock.count()).thenReturn(count);

        // execute
        var response = svc.getMemberCount();

        // verify
        assertEquals(count,response);
        verify(repoMock).count();
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

}
