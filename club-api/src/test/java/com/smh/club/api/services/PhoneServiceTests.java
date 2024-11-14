package com.smh.club.api.services;

import com.smh.club.api.common.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
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
public class PhoneServiceTests extends ServiceTests {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private PhoneRepo phnRepoMock;
    @Mock private PhoneMapper phnMapMock;

    @InjectMocks private PhoneServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<PhoneEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Test
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(params);

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
        svc.getPhoneListPage(params);

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
        svc.getPhoneListPage(params);

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
        svc.getPhoneListPage(params);

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
        assertThrows(IllegalArgumentException.class, () -> svc.getPhoneListPage(params));
        verifyNoInteractions(phnRepoMock);
    }

    @Test
    public void getItemListPage_returns_phoneList() {
        // setup
        var size = 10;
        var entityList = Instancio.ofList(PhoneEntity.class).size(size).create();
        var dtoList = Instancio.ofList(PhoneDto.class).size(size).create();

        var page = createEntityPage(entityList, pageableMock, 200);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(phnMapMock.toDtoList(page.getContent())).thenReturn(dtoList);

        // execute
        var pageResponse = svc.getPhoneListPage(PageParams.getDefault());

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
        var id = 1;
        var entity = Instancio.of(PhoneEntity.class).set(field(PhoneEntity::getId), id).create();
        var phone = Instancio.of(PhoneDto.class).set(field(PhoneDto::getId), id).create();

        when(phnRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(phnMapMock.toDto(entity)).thenReturn(phone);

        // execute
        var member = svc.getPhone(id);

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
        var ret = svc.getPhone(id);

        // and verify
        assertFalse(ret.isPresent());
        verify(phnRepoMock).findById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_phone() {
        // setup
        var member = Instancio.create(MemberEntity.class);
        when(memRepoMock.getReferenceById(member.getId())).thenReturn(member);

        var create = Instancio.of(CreatePhoneDto.class)
                .set(field(CreatePhoneDto::getMemberId), member.getId())
                .create();
        var phone = Instancio.of(PhoneDto.class)
                .set(field(PhoneDto::getMemberId), member.getId())
                .create();

        var entity = Instancio.of(PhoneEntity.class)
                .set(field(PhoneEntity::getMember), member)
                .create();

        when(phnRepoMock.save(entity)).thenReturn(entity);
        when(phnMapMock.toEntity(create)).thenReturn(entity);
        when(phnMapMock.toDto(entity)).thenReturn(phone);

        // execute
        var ret = svc.createPhone(create);

        // verify
        assertNotNull(ret);
        assertEquals(phone, ret);
        verify(memRepoMock).getReferenceById(member.getId());
        verify(phnRepoMock).save(entity);
        verify(phnMapMock).toEntity(create);
        verify(phnMapMock).toDto(entity);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_phone() {
        // setup
        int id = 1;
        var entity = Instancio.create(PhoneEntity.class);
        var update = Instancio.of(CreatePhoneDto.class)
                .set(field(CreatePhoneDto::getMemberId), id)
                .create();
        var phone = Instancio.create(PhoneDto.class);

        when(phnRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(phnMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(phnMapMock.toDto(entity)).thenReturn(phone);

        // execute
        var ret = svc.updatePhone(id, update);

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
        svc.deletePhone(id);

        //verify
        verify(phnRepoMock).deleteById(id);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

    @Test
    public void getCount_returns_phone_count() {
        // setup
        long count = 5;
        when(phnRepoMock.count()).thenReturn(count);

        // execute
        var response = svc.getPhoneCount();

        // verify
        assertEquals(count, response.getCount());
        verify(phnRepoMock).count();
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }
}
