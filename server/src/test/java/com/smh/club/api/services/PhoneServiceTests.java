package com.smh.club.api.services;

import com.smh.club.api.contracts.mappers.PhoneMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
import com.smh.club.api.dto.phone.PhoneCreateDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneFullNameDto;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class PhoneServiceTests extends ServiceTests {
    
    @Mock private MembersRepo memRepoMock;
    @Mock private PhoneRepo phnRepoMock;
    @Mock private PhoneMapper phnMapMock;

    @InjectMocks private PhoneServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<PhoneEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings // Instancio settings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @ParameterizedTest
    @CsvSource({"id, id", "phone_number, phoneNumber", "phone_type, phoneType"})
    public void getPhoneListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(PhoneFullNameDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(phnMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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

        verifyNoMoreInteractions(phnRepoMock, phnMapMock);
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

        var list = Instancio.ofList(PhoneFullNameDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(phnMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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
        verify(phnMapMock).toPage(pageMock);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock);
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

        var list = Instancio.ofList(PhoneFullNameDto.class)
            .size(pageSize)
            .create();

        var page = createPage(list, pageableMock, total);

        when(phnRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(phnMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        var ret = svc.getPage(pageable);

        // verify
        assertEquals(total, ret.getMetadata().totalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(phnRepoMock).findAll(any(PageRequest.class));
        verify(phnMapMock).toPage(pageMock);
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
    public void create_phone_returns_phone() {
        // setup
        var member = Instancio.create(MemberEntity.class);
        when(memRepoMock.getReferenceById(member.getId())).thenReturn(member);

        var create = Instancio.of(PhoneCreateDto.class)
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

    @Test
    public void getByMemberId_returns_phone_list() {
        // setup
        var member = Instancio.create(MemberEntity.class);

        var entities = Instancio.ofList(PhoneEntity.class)
            .size(5).create();

        var dtoList = Instancio.ofList(PhoneDto.class)
            .size(5).create();
        dtoList.forEach(dto -> dto.setMemberId(member.getId()));

        when(phnRepoMock.findAllByMemberId(member.getId())).thenReturn(entities);
        when(phnMapMock.toDtoList(entities)).thenReturn(dtoList);

        // execute
        var ret = svc.findAllByMemberId(member.getId());

        // verify
        assertNotNull(ret);
        assertEquals(dtoList, ret);
        verify(phnRepoMock).findAllByMemberId(member.getId());
        verify(phnMapMock).toDtoList(entities);
        verifyNoMoreInteractions(phnRepoMock, phnMapMock, memRepoMock);
    }
}
