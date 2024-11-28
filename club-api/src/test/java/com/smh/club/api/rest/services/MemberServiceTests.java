package com.smh.club.api.rest.services;

import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.rest.contracts.mappers.MemberMapper;
import com.smh.club.api.rest.dto.MemberDetailDto;
import com.smh.club.api.rest.dto.MemberDto;
import java.util.Optional;
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

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.MAX_DEPTH, 4);

    @ParameterizedTest
    @CsvSource({"id, id", "member-number, memberNumber", "first-name, firstName", "last-name, lastName",
                "birth-date, birthDate", "joined-date, joinedDate"})
    public void getMemberListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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
    @ValueSource(strings = {"middle-name", "middleName", "suffix"})
    public void getMemberListPage_excludes_use_memberId(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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

    @Test
    public void getMemberListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "memberNumber";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(repoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());
        verify(repoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void getMemberListPage_with_desc() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getMemberListPage(pageNumber, pageSize, direction, sort);

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
        verifyNoMoreInteractions(repoMock);
    }


    @Test
    public void getMemberListPage_returns_MemberList() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "id";
        var total = 200;
        var entityList = Instancio.ofList(MemberEntity.class).size(pageSize).create();
        var dto = Instancio.of(MemberDto.class).create();

        var page = createPage(entityList, pageableMock, total);

        when(repoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(mapperMock.toDto(any(MemberEntity.class))).thenReturn(dto);

        // execute
        var ret = svc.getMemberListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(total, ret.getTotalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(repoMock).findAll(any(PageRequest.class));
        verify(mapperMock, times(pageSize)).toDto(any(MemberEntity.class));
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
        var create = Instancio.create(MemberDto.class);
        var member = Instancio.create(MemberDto.class);
        var entity = Instancio.create(MemberEntity.class);

        when(repoMock.save(entity)).thenReturn(entity);
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
