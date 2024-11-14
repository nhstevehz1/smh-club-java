package com.smh.club.api.services;

import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
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

    @Test
    public void getRenewalListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(params);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "renewal-year");
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(params);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("renewalYear", order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(params);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getRenewalListPage(params);

        // verify
        verify(renRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(renRepoMock);
    }

    @Test
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "renewal-year");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getRenewalListPage(params));
        verifyNoInteractions(renRepoMock);
    }

    @Test
    public void getRenewalListPage_returns_renewalList() {
        // setup
        var size = 10;
        var entityList = Instancio.ofList(RenewalEntity.class).size(size).create();
        var dtoList = Instancio.ofList(RenewalDto.class).size(size).create();

        var page = createEntityPage(entityList, pageableMock, 200);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(renMapMock.toDtoList(page.getContent())).thenReturn(dtoList);

        // execute
        var pageResponse = svc.getRenewalListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verify(renMapMock).toDtoList(page.getContent());
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_renewal() {
        // setup
        int id = 1;
        var entity = Instancio.of(RenewalEntity.class).set(field(RenewalEntity::getId), id).create();
        var address = Instancio.of(RenewalDto.class).set(field(RenewalDto::getId), id).create();

        when(renRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(renMapMock.toDto(entity)).thenReturn(address);

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

        var create = Instancio.of(CreateRenewalDto.class)
                .set(field(CreateRenewalDto::getMemberId), member.getId())
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
        var update = Instancio.of(UpdateRenewalDto.class)
                .set(field(UpdateRenewalDto::getMemberId), id)
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
