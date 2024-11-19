package com.smh.club.api.rest.services;

import com.smh.club.api.data.contracts.mappers.EmailMapper;
import com.smh.club.api.data.domain.entities.EmailEntity;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.EmailRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.dto.EmailDto;
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
public class EmailServiceTests extends ServiceTests {

    @Mock private MembersRepo memRepoMock;
    @Mock private EmailRepo emailRepoMock;
    @Mock private EmailMapper emailMapMock;

    @InjectMocks private EmailServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<EmailEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

   @ParameterizedTest
   @CsvSource({"id, id", "email, email", "email-type, emailType"})
    public void getEmailListPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(emailRepoMock, emailMapMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"memberId", "member-id"})
    public void getEmailListPage_excludes_use_id(String sort) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var actual = "id";

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(actual, order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));


        verifyNoMoreInteractions(emailRepoMock, emailMapMock);
    }

    @Test
    public void getEmailListPage_with_empty_sort_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "";
        var defaultSort = "id";

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());

        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getEmailListPage_unknown_sortColumn_uses_default() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
        var sort = "thisIsNotAColumn";
        var defaultSort = "id";

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(defaultSort, order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getEmailListPage_with_desc() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "email";

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(pageSize, pageRequest.getPageSize());
        assertEquals(pageNumber, pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertTrue(direction.equalsIgnoreCase(order.getDirection().toString()));
        assertEquals(sort, order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }


    @Test
    public void getEmailListPage_returns_EmailList() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "desc";
        var sort = "email";
        var total = 200;
        var entityList = Instancio.ofList(EmailEntity.class).size(pageSize).create();
        var dto = Instancio.of(EmailDto.class).create();

        var page = createEntityPage(entityList, pageableMock, total);

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(emailMapMock.toDto(any(EmailEntity.class))).thenReturn(dto);

        // g
        var ret = svc.getEmailListPage(pageNumber, pageSize, direction, sort);

        // verify
        assertEquals(total, ret.getTotalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verify(emailMapMock, times(pageSize)).toDto(any(EmailEntity.class));
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_email() {
        // setup
        int id = 1;
        var entity = Instancio.of(EmailEntity.class).set(field(EmailEntity::getId), id).create();
        var email = Instancio.of(EmailDto.class).set(field(EmailDto::getId), id).create();

        when(emailRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(emailMapMock.toDto(any(EmailEntity.class))).thenReturn(email);

        // execute
        var ret = svc.getEmail(id);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findById(id);
        verify(emailMapMock).toDto(any(EmailEntity.class));
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(emailRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getEmail(id);

        // verify
        assertFalse(ret.isPresent());
        verify(emailRepoMock).findById(id);
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }

    @Test
    public void create_email_returns_emailDto() {
        // setup
        var member = Instancio.create(MemberEntity.class);
        when(memRepoMock.getReferenceById(member.getId())).thenReturn(member);

        var create = Instancio.of(EmailDto.class)
                .set(field(EmailDto::getMemberId), member.getId())
                .create();
        var email = Instancio.of(EmailDto.class)
                .set(field(EmailDto::getMemberId), member.getId())
                .create();

        var entity = Instancio.of(EmailEntity.class)
                .set(field(EmailEntity::getMember), member)
                .create();

        when(emailRepoMock.save(entity)).thenReturn(entity);
        when(emailMapMock.toEntity(create)).thenReturn(entity);
        when(emailMapMock.toDto(entity)).thenReturn(email);

        // execute
        var ret = svc.createEmail(create);

        // verify
        assertNotNull(email);
        assertEquals(email, ret);
        verify(memRepoMock).getReferenceById(member.getId());
        verify(emailRepoMock).save(entity);
        verify(emailMapMock).toEntity(create);
        verify(emailMapMock).toDto(entity);
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_email() {
        // setup
        int id = 1;
        var entity = Instancio.create(EmailEntity.class);
        var update = Instancio.of(EmailDto.class)
                .set(field(EmailDto::getMemberId), id)
                .create();
        var email = Instancio.create(EmailDto.class);

        when(emailRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(emailMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(emailMapMock.toDto(entity)).thenReturn(email);

        // execute
        var ret = svc.updateEmail(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findByIdAndMemberId(id, id);

        verify(emailMapMock).updateEntity(update, entity);
        verify(emailMapMock).toDto(entity);
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_email() {
        // setup
        int id = 1;
        doNothing().when(emailRepoMock).deleteById(id);

        // execute
        svc.deleteEmail(id);

        //verify
        verify(emailRepoMock).deleteById(id);
        verifyNoMoreInteractions(emailRepoMock, emailMapMock, memRepoMock);
    }
}
