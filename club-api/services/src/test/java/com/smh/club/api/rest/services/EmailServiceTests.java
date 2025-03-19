package com.smh.club.api.rest.services;

import com.smh.club.api.rest.contracts.mappers.EmailMapper;
import com.smh.club.api.rest.domain.entities.EmailEntity;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.EmailRepo;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.email.EmailCreateDto;
import com.smh.club.api.rest.dto.email.EmailDto;
import com.smh.club.api.rest.dto.email.EmailFullNameDto;
import com.smh.club.api.rest.dto.email.EmailUpdateDto;
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
public class EmailServiceTests extends ServiceTests {

    @Mock private MembersRepo memRepoMock;
    @Mock private EmailRepo emailRepoMock;
    @Mock private EmailMapper emailMapMock;

    @InjectMocks private EmailServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<EmailEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @WithSettings // Instancio settings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

   @ParameterizedTest
   @CsvSource({"id, id", "email, email", "email_type, emailType",
       "member_number, member.memberNumber","full_name, member.lastName"})
    public void getPage(String sort, String actual) {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "ASC";
       var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
       var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

       var list = Instancio.ofList(EmailFullNameDto.class)
           .size(20)
           .create();

       var page = createPage(list, pageableMock,100);

       when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
       when(emailMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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

        verifyNoMoreInteractions(emailRepoMock, emailMapMock);
    }

    @Test
    public void getPage_with_descending() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "DESC";
        var sort = "email";
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(EmailFullNameDto.class)
            .size(20)
            .create();

        var page = createPage(list, pageableMock,100);

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(emailMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        svc.getPage(pageable);

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
        verify(emailMapMock).toPage(pageMock);
        verifyNoMoreInteractions(emailRepoMock, emailMapMock);
    }


    @Test
    public void getPage_returns_list() {
        // setup
        var pageNumber = 10;
        var pageSize = 20;
        var direction = "DESC";
        var sort = "email";
        var total = 200;
        var orderRequest = new Sort.Order(Sort.Direction.valueOf(direction), sort);
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderRequest));

        var list = Instancio.ofList(EmailFullNameDto.class)
            .size(pageSize)
            .create();

        var page = createPage(list, pageableMock, total);

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);
        when(emailMapMock.toPage(pageMock)).thenReturn(page);

        // execute
        var ret = svc.getPage(pageable);

        // verify
        assertEquals(total, ret.getMetadata().totalElements());
        assertEquals(pageSize, ret.getContent().size());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verify(emailMapMock).toPage(pageMock);
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

        var create = Instancio.of(EmailCreateDto.class)
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

        var update = Instancio.of(EmailUpdateDto.class)
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
