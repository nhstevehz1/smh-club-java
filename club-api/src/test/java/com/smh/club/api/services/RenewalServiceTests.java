package com.smh.club.api.services;

import com.smh.club.api.Services.RenewalServiceIml;
import com.smh.club.api.common.mappers.RenewalMapper;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.entities.RenewalEntity;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.data.repos.RenewalsRepo;
import com.smh.club.api.models.Renewal;
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

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RenewalServiceTests extends CrudServiceTestBase<Renewal, RenewalEntity> {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private RenewalsRepo renRepoMock;
    @Mock private RenewalMapper renMapMock;

    @InjectMocks
    private RenewalServiceIml svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<RenewalEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_defaultPageParams() {
        // setup
        var params = PageParams.getDefault();
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_nonDefaultPageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "renewal-year");
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsUnknownSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsNullSortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        this.svc.getItemListPage(params);

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
    public void getItemListPage_PageParamsNullSortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "renewal-year");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> this.svc.getItemListPage(params));
        verifyNoInteractions(renRepoMock);
    }

    @Test
    public void getItemListPage_returnsRenewalList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(renRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(renMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = this.svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(renRepoMock).findAll(any(PageRequest.class));
        verify(renMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_returnsRenewal() {
        // setup
        int id = 1;
        var optional = Optional.of(createEntity(id));
        when(renRepoMock.findById(id)).thenReturn(optional);
        when(renMapMock.toDataObject(any(RenewalEntity.class))).thenReturn(createDataObject(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertNotNull(member);
        verify(renRepoMock).findById(id);
        verify(renMapMock).toDataObject(any(RenewalEntity.class));
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_ThrowsException() {
        // setup
        int id = 1;
        Optional<RenewalEntity> optional = Optional.empty();
        when(renRepoMock.findById(id)).thenReturn(optional);

        // execute and verify
        assertThrows(NoSuchElementException.class, () -> svc.getItem(id));
        verify(renRepoMock).findById(id);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void createItem_returnsRenewal() {
        // setup
        var memberId = 10;
        var m1 = MemberEntity.builder().id(memberId).build();
        var optional = Optional.of(m1);
        when(memRepoMock.findById(memberId)).thenReturn(optional);

        var a1 = createDataObject(1);
        a1.setMemberId(memberId);
        var entity = createEntity(1);
        when(renRepoMock.save(entity)).thenReturn(entity);
        when(renMapMock.toEntity(a1)).thenReturn(entity);
        when(renMapMock.toDataObject(entity)).thenReturn(a1);

        // execute
        var renewal = svc.createItem(a1);

        // verify
        assertNotNull(renewal);
        assertEquals(a1, renewal);
        verify(memRepoMock).findById(memberId);
        verify(renRepoMock).save(entity);
        verify(renMapMock).toEntity(a1);
        verify(renMapMock).toDataObject(entity);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returnsRenewal() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var e1 = createDataObject(id);
        var optional = Optional.of(entity);

        when(renRepoMock.findById(id)).thenReturn(optional);
        when(renRepoMock.save(entity)).thenReturn(entity);

        doNothing().when(renMapMock).updateEntity(e1, entity);
        when(renMapMock.toDataObject(entity)).thenReturn(e1);

        // execute
        var member = svc.updateItem(id, e1);

        // verify
        assertNotNull(member);
        verify(renRepoMock).findById(id);
        verify(renRepoMock).save(entity);

        verify(renMapMock).updateEntity(e1, entity);
        verify(renMapMock).toDataObject(entity);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletesRenewal() {
        // setup
        int id = 1;
        doNothing().when(renRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(renRepoMock).deleteById(id);
        verifyNoMoreInteractions(renRepoMock, renMapMock, memRepoMock);
    }
    
    @Override
    protected RenewalEntity createEntity(int flag) {
        var now = LocalDate.now();
        
        return RenewalEntity.builder()
                .id(flag)
                .renewalDate(now)
                .renewalYear(Integer.toString(now.getYear()))
                .build();
    }
    
    @Override
    protected Renewal createDataObject(int flag) {
        var now = LocalDate.now();
        
        return Renewal.builder()
                .id(flag)
                .memberId(flag)
                .renewalDate(now)
                .renewalYear(Integer.toString(now.getYear()))
                .build();
    }
}
