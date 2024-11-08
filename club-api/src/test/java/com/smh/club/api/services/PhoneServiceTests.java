package com.smh.club.api.services;

import com.smh.club.api.Services.PhoneServiceImpl;
import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
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

import static com.smh.club.api.helpers.datacreators.PhoneCreators.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneServiceTests extends ServiceTests {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private PhoneRepo phnRepoMock;
    @Mock private PhoneMapper phnMapMock;

    @InjectMocks private PhoneServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<PhoneEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "phone-number");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("phoneNum", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_unknown_sortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_null_sortColumn() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getItemListPage(params);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "phoneNum");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getItemListPage(params));
        verifyNoInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_returns_phoneList() {
        // setup
        var entityList = genPhoneEntityList(40);
        var page = createEntityPage(entityList, pageableMock, 200);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(phnMapMock.toDtoList(page.getContent())).thenReturn(genPhoneDtoList(40));

        // execute
        var pageResponse = svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verify(phnMapMock).toDtoList(page.getContent());
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_phone() {
        // setup
        int id = 1;
        var entity = genPhoneEntity(id);
        when(phnRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(phnMapMock.toDto(entity)).thenReturn(genPhoneDto(id));

        // execute
        var member = svc.getItem(id);

        // verify
        assertTrue(member.isPresent());
        verify(phnRepoMock).findById(id);
        verify(phnMapMock).toDto(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(phnRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getItem(id);

        // and verify
        assertFalse(ret.isPresent());
        verify(phnRepoMock).findById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_phone() {
        // setup
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var create = genCreatePhoneDto(1);
        create.setMemberId(memberId);
        var phone = genPhoneDto(1);

        var entity = genPhoneEntity(1);
        when(phnRepoMock.save(entity)).thenReturn(entity);
        when(phnMapMock.toEntity(create)).thenReturn(entity);
        when(phnMapMock.toDto(entity)).thenReturn(phone);

        // execute
        var ret = svc.createItem(create);

        // verify
        assertNotNull(ret);
        assertEquals(phone, ret);
        verify(memRepoMock).getReferenceById(memberId);
        verify(phnRepoMock).save(entity);
        verify(phnMapMock).toEntity(create);
        verify(phnMapMock).toDto(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_phone() {
        // setup
        int id = 1;
        var entity = genPhoneEntity(id);
        var update = genUpdatePhoneDto(id);
        var phone = genPhoneDto(id);

        when(phnRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(phnMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(phnMapMock.toDto(entity)).thenReturn(phone);

        // execute
        var ret = svc.updateItem(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(phnRepoMock).findByIdAndMemberId(id, id);

        verify(phnMapMock).updateEntity(update, entity);
        verify(phnMapMock).toDto(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_phone() {
        // setup
        int id = 1;
        doNothing().when(phnRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(phnRepoMock).deleteById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }
}
