package com.smh.club.api.services;

import com.smh.club.api.contracts.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.renewal.RenewalCreateDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalFullNameDto;
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
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

    @WithSettings // Instancio settings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @ParameterizedTest
    @CsvSource({"id, id", "renewal_date, renewalDate", "renewal_year, renewalYear"})
    public void getPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(RenewalFullNameDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(renMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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
    public void getPage_excludes_throws_exception(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getPage(pageable));
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

        verifyNoMoreInteractions(renRepoMock, renMapMock);
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

        var list = Instancio.ofList(RenewalFullNameDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(renMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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
        verify(renMapMock).toPage(pageMock);
        verifyNoMoreInteractions(renRepoMock, renMapMock);
    }

    @Test
    public void getPage_returns_list() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "DESC";
        var sort = "id";
        var total = 200;
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(RenewalFullNameDto.class)
            .size(pageSize)
            .create();

        var page = createPage(list, pageableMock, total);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(renMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        var ret = svc.getPage(pageable);

        // verify
        assertEquals(total, ret.getMetadata().totalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verify(renMapMock).toPage(pageMock);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_renewal() {
        // setup
        int id = 1;
        var entity = Instancio.of(RenewalEntity.class).set(field(RenewalEntity::getId), id).create();
        var renewal = Instancio.of(RenewalDto.class).set(field(RenewalDto::getId), id).create();

        when(renRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(renMapMock.toDto(entity)).thenReturn(renewal);

        // execute
        var ret = svc.getRenewal(id);

        // verify
        assertNotNull(ret);
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

        var create = Instancio.of(RenewalCreateDto.class)
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

    @Test
    public void getByMemberId_returns_renewal_list() {
        // setup
        var member = Instancio.create(MemberEntity.class);

        var entities = Instancio.ofList(RenewalEntity.class)
            .size(5).create();

        var dtoList = Instancio.ofList(RenewalDto.class)
            .size(5).create();
        dtoList.forEach(dto -> dto.setMemberId(member.getId()));

        when(renRepoMock.findAllByMemberId(member.getId())).thenReturn(entities);
        when(renMapMock.toDtoList(entities)).thenReturn(dtoList);

        // execute
        var ret = svc.findAllByMemberId(member.getId());

        // verify
        assertNotNull(ret);
        assertEquals(dtoList, ret);
        verify(renRepoMock).findAllByMemberId(member.getId());
        verify(renMapMock).toDtoList(entities);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }
}
