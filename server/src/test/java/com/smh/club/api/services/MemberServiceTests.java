package com.smh.club.api.services;

import java.util.Optional;

import com.smh.club.api.contracts.mappers.MemberMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.member.MemberCreateDto;
import com.smh.club.api.dto.member.MemberDetailDto;
import com.smh.club.api.dto.member.MemberDto;
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

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class MemberServiceTests extends ServiceTests {

    @Mock private MembersRepo repoMock;
    @Mock private MemberMapper mapperMock;

    @InjectMocks
    MemberServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<MemberEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings // Instancio settings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);

    @ParameterizedTest
    @CsvSource({"id, id", "member_number, memberNumber", "first_name, firstName", "last_name, lastName",
                "birth_date, birthDate", "joined_date, joinedDate"})
    public void getPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(MemberDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(mapperMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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

        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"middle_name", "middleName", "suffix"})
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

        verifyNoMoreInteractions(repoMock, mapperMock);
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

        var list = Instancio.ofList(MemberDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(mapperMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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
        verify(mapperMock).toPage(pageMock);
        verifyNoMoreInteractions(repoMock, mapperMock);
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

        var list = Instancio.ofList(MemberDto.class)
            .size(pageSize)
            .create();

        var page = createPage(list, pageableMock, total);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(mapperMock.toPage(pageMock)).thenReturn(page);

        // execute
        var ret = svc.getPage(pageable);

        // verify
        assertEquals(total, ret.getMetadata().totalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(mapperMock).toPage(pageMock);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_returns_member() {
        // setup
        int id = 1;
        var entity = Instancio.of(MemberEntity.class).set(field(MemberEntity::getId), id).create();
        var member = Instancio.of(MemberDto.class).set(field(MemberDto::getId), id).create();

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toDto(any(MemberEntity.class))).thenReturn(member);

        // execute
        var ret = svc.getMember(id);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);
        verify(mapperMock).toDto(any(MemberEntity.class));
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getMember(id);

        //
        assertFalse(ret.isPresent());
        verify(repoMock).findById(id);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void createMember_returns_member() {
        // setup
        var create = Instancio.create(MemberCreateDto.class);
        var member = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);

        when(repoMock.save(entity)).thenReturn(entity);
        when(repoMock.findMaxMemberNumber()).thenReturn(Optional.of(1));
        when(mapperMock.toEntity(create)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(member);

        // execute
        var ret = svc.createMember(create);

        // verify
        assertNotNull(ret);
        assertEquals(ret, member);
        verify(repoMock).save(entity);
        verify(mapperMock).toEntity(create);
        verify(mapperMock).toDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
    }

    @Test
    public void updateMember_returns_member() {
        // setup
        int id = 1;
        var update = Instancio.create(MemberDto.class);
        var member = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));

        when(mapperMock.updateEntity(update, entity)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(member);

        // execute
        var ret = svc.updateMember(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(repoMock).findById(id);

        verify(mapperMock).updateEntity(update, entity);
        verify(mapperMock).toDto(entity);
        verifyNoMoreInteractions(repoMock, mapperMock);
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

    @Test
    public void getMemberDetail_returns_memberDetail() {
        // setup
        int id = 1;
        var entity = Instancio.create(MemberEntity.class);
        var memberDetail = Instancio.create(MemberDetailDto.class);

        when(repoMock.findById(id)).thenReturn(Optional.of(entity));
        when(mapperMock.toMemberDetailDto(entity)).thenReturn(memberDetail);

        var ret = svc.getMemberDetail(id);

        // verify
        assertTrue(ret.isPresent());

        var detail = ret.get();
        assertEquals(memberDetail.getId(), detail.getId());
        assertNotNull(detail.getAddresses());
        assertNotNull(detail.getEmails());
        assertNotNull(detail.getPhones());
        assertNotNull(detail.getRenewals());

        verify(repoMock).findById(id);
        verify(mapperMock).toMemberDetailDto(entity);
        verifyNoMoreInteractions(mapperMock);
    }

    @Test
    public void getMemberDetail_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(repoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getMemberDetail(id);

        // verify
        assertFalse(ret.isPresent());
        verify(repoMock).findById(1);
        verifyNoMoreInteractions(mapperMock);
    }

}
