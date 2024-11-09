package com.smh.club.api.services;

import com.smh.club.api.Services.RenewalServiceIml;
import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
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

import static com.smh.club.api.helpers.datacreators.RenewalCreators.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RenewalServiceTests extends ServiceTests {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private RenewalsRepo renRepoMock;
    @Mock private RenewalMapper renMapMock;

    @InjectMocks
    private RenewalServiceIml svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<RenewalEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_uses_default_pageParams() {
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
    public void getItemListPage_nonDefault_pageParams() {
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
    public void getItemListPage_unknown_sortColumn_uses_default() {
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
    public void getItemListPage_null_sortColumn_uses_default() {
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
    public void getItemListPage_returns_renewalList() {
        // setup
        var entityList = genRenewalEntityList(10);
        var page = createEntityPage(entityList, pageableMock, 200);

        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(renMapMock.toDtoList(page.getContent())).thenReturn(genRenewalDtoList(10));

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
        var entity = genRenewalEntity(id);
        when(renRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(renMapMock.toDto(any(RenewalEntity.class))).thenReturn(genRenewalDto(id));

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
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var create = genCreateRenewalDto(1);
        create.setMemberId(memberId);
        var renewal = genRenewalDto(1);

        var entity = genRenewalEntity(1);

        when(renRepoMock.save(entity)).thenReturn(entity);
        when(renMapMock.toEntity(create)).thenReturn(entity);
        when(renMapMock.toDto(entity)).thenReturn(renewal);

        // execute
        var ret = svc.createRenewal(create);

        // verify
        assertNotNull(ret);
        assertEquals(renewal, ret);
        verify(memRepoMock).getReferenceById(memberId);
        verify(renRepoMock).save(entity);
        verify(renMapMock).toEntity(create);
        verify(renMapMock).toDto(entity);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_renewal() {
        // setup
        int id = 1;
        var entity = genRenewalEntity(id);
        var update = genUpdateRenewalDto(id);
        var renewal = genRenewalDto(id);
        
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
