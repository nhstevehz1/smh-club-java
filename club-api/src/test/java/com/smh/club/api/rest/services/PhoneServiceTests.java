package com.smh.club.api.rest.services;

import com.smh.club.api.data.contracts.mappers.PhoneMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.entities.PhoneEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.domain.repos.PhoneRepo;
import com.smh.club.api.data.dto.PhoneDto;
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

    @ParameterizedTest
    @CsvSource({"id, id", "phone-number, phoneNumber", "phone-type, phoneType"})
    public void getPhoneListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(phnRepoMock, phnMapMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"member-id", "memberId"})
    public void getPhoneListPage_excludes_use_id(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "id";

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(phnRepoMock, phnMapMock);
    }

    @Test
    public void getphnMapMockListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "id";

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getphnMapMockListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "id";

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }

    @Test
    public void getPhoneMapMockListPage_with_desc() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(phnRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(sort, order.getProperty());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(phnRepoMock);
    }


    @Test
    public void getphnMapMockListPage_returns_phnMapMockList() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "phone";
        var total = 200;
        var entityList = Instancio.ofList(PhoneEntity.class).size(pageSize).create();
        var dto = Instancio.of(PhoneDto.class).create();

        var page = createEntityPage(entityList, pageableMock, total);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(phnMapMock.toDto(any(PhoneEntity.class))).thenReturn(dto);

        // execute
        var ret = svc.getPhoneListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(total, ret.getTotalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verify(phnMapMock, times(pageSize)).toDto(any(PhoneEntity.class));
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

        var create = Instancio.of(PhoneDto.class)
                .set(field(PhoneDto::getMemberId), member.getId())
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
        var update = Instancio.of(PhoneDto.class)
                .set(field(PhoneDto::getMemberId), id)
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
        assertEquals(count, response);
        verify(phnRepoMock).count();
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }

}
