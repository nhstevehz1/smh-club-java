package com.smh.club.api.rest.services;

import com.smh.club.api.data.contracts.mappers.RenewalMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.entities.RenewalEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.RenewalsRepo;
import com.smh.club.api.data.dto.RenewalDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RenewalServiceTests extends ServiceTests {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private RenewalsRepo renRepoMock;
    @Mock private RenewalMapper renMapMock;

    @InjectMocks
    private RenewalServiceIml svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<RenewalEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @ParameterizedTest
    @CsvSource({"id, id", "renewal-date, renewalDate", "renewal-year, renewalYear"})
    public void getRenewalListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(renRepoMock, renMapMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id", "memberId"})
    public void getRenewalListPage_excludes_use_id(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "id";

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(renRepoMock, renMapMock);
    }

    @Test
    public void getRenewalListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "id";

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "id";

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_with_desc() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(sort, order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }


    @Test
    public void getRenewalListPage_returns_RenewalList() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";
        var total = 200;
        var entityList = Instancio.ofList(RenewalEntity.class).size(pageSize).create();
        var dto = Instancio.of(RenewalDto.class).create();

        var page = createEntityPage(entityList, pageableMock, total);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(renMapMock.toDto(any(RenewalEntity.class))).thenReturn(dto);

        // execute
        var ret = svc.getRenewalListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(total, ret.getTotalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verify(renMapMock, times(pageSize)).toDto(any(RenewalEntity.class));
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_renewal() {
        // setup
        int id = 1;
        var entity = Instancio.of(RenewalEntity.class).set(field(RenewalEntity::getId), id).create();
        var dto = Instancio.of(RenewalDto.class).set(field(RenewalDto::getId), id).create();

        when(renRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(renMapMock.toDto(entity)).thenReturn(dto);

        // execute
        var member = svc.getRenewal(id);

        // verify
        assertNotNull(member);
        verify(renRepoMock).findById(id);
        verify(renMapMock).toDto(any(RenewalEntity.class));
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(renRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getRenewal(id);

        //verify
        assertFalse(ret.isPresent());
        verify(renRepoMock).findById(id);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_renewal() {
        // setup
        var member = Instancio.create(MemberEntity.class);
        when(memRepoMock.getReferenceById(member.getId())).thenReturn(member);

        var create = Instancio.of(RenewalDto.class)
                .set(field(RenewalDto::getMemberId), member.getId())
                .create();
        var renewal = Instancio.of(RenewalDto.class)
                .set(field(RenewalDto::getMemberId), member.getId())
                .create();

        var entity = Instancio.of(RenewalEntity.class)
                .set(field(RenewalEntity::getMember), member)
                .create();

        when(renRepoMock.save(entity)).thenReturn(entity);
        when(renMapMock.toEntity(create)).thenReturn(entity);
        when(renMapMock.toDto(entity)).thenReturn(renewal);

        // execute
        var ret = svc.createRenewal(create);

        // verify
        assertNotNull(ret);
        assertEquals(renewal, ret);
        verify(memRepoMock).getReferenceById(member.getId());
        verify(renRepoMock).save(entity);
        verify(renMapMock).toEntity(create);
        verify(renMapMock).toDto(entity);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_renewal() {
        // setup
        int id = 1;
        var entity = Instancio.create(RenewalEntity.class);
        var update = Instancio.of(RenewalDto.class)
                .set(field(RenewalDto::getMemberId), id)
                .create();
        var renewal = Instancio.create(RenewalDto.class);
        
        when(renRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));
        when(renMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(renMapMock.toDto(entity)).thenReturn(renewal);

        // execute
        var ret = svc.updateRenewal(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(renRepoMock).findByIdAndMemberId(id, id);

        verify(renMapMock).updateEntity(update, entity);
        verify(renMapMock).toDto(entity);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_renewal() {
        // setup
        int id = 1;
        doNothing().when(renRepoMock).deleteById(id);

        // execute
        svc.deleteRenewal(id);

        //verify
        verify(renRepoMock).deleteById(id);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }
}
