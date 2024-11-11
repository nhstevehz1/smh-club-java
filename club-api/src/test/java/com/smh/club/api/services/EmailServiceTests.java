package com.smh.club.api.services;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.EmailRepo;
import com.smh.club.api.domain.repos.MembersRepo;
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

import static com.smh.club.api.helpers.datacreators.EmailCreators.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests extends ServiceTests {

    @Mock private MembersRepo memRepoMock;
    @Mock private EmailRepo emailRepoMock;
    @Mock private EmailMapper emlMapMock;

    @InjectMocks private EmailServiceImpl svc;

    @Mock private Pageable pageableMock;
    @Mock private Page<EmailEntity> pageMock;

    @Captor private ArgumentCaptor<PageRequest> acPageRequest;

    @Test
    public void getItemListPage_with_default_pageParams() {
        // setup
        var params = PageParams.getDefault();
        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(params);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_with_nonDefault_pageParams() {
        // setup
        var params = createPageParam(5,100, Sort.Direction.DESC, "email");
        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(params);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("email", order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_unknown_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, "thisIsNotAColumn");
        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(params);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_null_sortColumn_uses_default() {
        // setup
        var params = createPageParam(5, 100, Sort.Direction.DESC, null);
        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(pageMock);

        // execute
        svc.getEmailListPage(params);

        // verify
        verify(emailRepoMock).findAll(acPageRequest.capture());

        var pageRequest = acPageRequest.getValue();
        assertEquals(params.getPageSize(), pageRequest.getPageSize());
        assertEquals(params.getPageNumber(), pageRequest.getPageNumber());

        // only one sort order is supported
        var order = pageRequest.getSort().get().findFirst().orElseThrow();
        assertEquals(params.sortDirection, order.getDirection());
        assertEquals("id", order.getProperty());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_null_sortDirection_throwsException() {
        // setup
        var params = createPageParam(5,100, null, "email");

        // execute and verify
        assertThrows(IllegalArgumentException.class, () -> svc.getEmailListPage(params));
        verifyNoInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_returns_emailList() {
        // setup
        var entityList = createEmailEntityList(40);
        var page = createEntityPage(entityList, pageableMock, 200);

        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(emlMapMock.toDtoList(page.getContent())).thenReturn(createEmailDtoList(40));

        // execute
        var pageResponse = svc.getEmailListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verify(emlMapMock).toDtoList(page.getContent());
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_email() {
        // setup
        int id = 1;
        var entity = genEmailEntity(id);
        when(emailRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(emlMapMock.toDto(any(EmailEntity.class))).thenReturn(createEmailDto(id));

        // execute
        var ret = svc.getEmail(id);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findById(id);
        verify(emlMapMock).toDto(any(EmailEntity.class));
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
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
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void create_email_returns_emailDto() {
        // setup
        var memberId = 1;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var create = genCreateEmailDto(1);
        var email = createEmailDto(1);
        email.setMemberId(memberId);

        var entity = genEmailEntity(1);

        when(emailRepoMock.save(entity)).thenReturn(entity);
        when(emlMapMock.toEntity(create)).thenReturn(entity);
        when(emlMapMock.toDto(entity)).thenReturn(email);

        // execute
        var ret = svc.createEmail(create);

        // verify
        assertNotNull(email);
        assertEquals(email, ret);
        verify(memRepoMock).getReferenceById(memberId);
        verify(emailRepoMock).save(entity);
        verify(emlMapMock).toEntity(create);
        verify(emlMapMock).toDto(entity);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_email() {
        // setup
        int id = 1;
        var entity = genEmailEntity(id);
        var update = genUpdateEmailDto(id);
        var email = createEmailDto(id);

        when(emailRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(emlMapMock.updateEntity(update, entity)).thenReturn(entity);
        when(emlMapMock.toDto(entity)).thenReturn(email);

        // execute
        var ret = svc.updateEmail(id, update);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findByIdAndMemberId(id, id);

        verify(emlMapMock).updateEntity(update, entity);
        verify(emlMapMock).toDto(entity);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
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
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }
}
