package com.smh.club.api.services;

import com.smh.club.api.Services.EmailServiceImpl;
import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.data.entities.EmailEntity;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.data.repos.EmailRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.Email;
import com.smh.club.api.models.EmailType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests extends CrudServiceTestBase<Email, EmailEntity> {

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
        svc.getItemListPage(params);

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
        svc.getItemListPage(params);

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
        svc.getItemListPage(params);

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
        svc.getItemListPage(params);

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
        assertThrows(IllegalArgumentException.class, () -> svc.getItemListPage(params));
        verifyNoInteractions(emailRepoMock);
    }

    @Test
    public void getItemListPage_returns_emailList() {
        // setup
        var page = createPage(10, pageableMock, 200);
        when(emailRepoMock.findAll(any(PageRequest.class))).thenReturn(page);
        when(emlMapMock.toDataObjectList(page.getContent())).thenReturn(createDataObjectList(10));

        // execute
        var pageResponse = svc.getItemListPage(PageParams.getDefault());

        // verify
        assertEquals(page.getTotalPages(), pageResponse.getTotalPages());
        assertEquals(page.getTotalElements(), pageResponse.getTotalCount());
        assertEquals(page.getContent().size(), pageResponse.getItems().size());
        verify(emailRepoMock).findAll(any(PageRequest.class));
        verify(emlMapMock).toDataObjectList(page.getContent());
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void getItem_returns_email() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        when(emailRepoMock.findById(id)).thenReturn(Optional.of(entity));
        when(emlMapMock.toDataObject(any(EmailEntity.class))).thenReturn(createDataObject(id));

        // execute
        var ret = svc.getItem(id);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findById(id);
        verify(emlMapMock).toDataObject(any(EmailEntity.class));
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void getItem_notFound_returns_empty_optional() {
        // setup
        int id = 1;
        when(emailRepoMock.findById(id)).thenReturn(Optional.empty());

        // execute
        var ret = svc.getItem(id);

        // verify
        assertFalse(ret.isPresent());
        verify(emailRepoMock).findById(id);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void createItem_returns_email() {
        // setup
        var memberId = 10;
        var member = MemberEntity.builder().id(memberId).build();
        when(memRepoMock.getReferenceById(memberId)).thenReturn(member);

        var email = createDataObject(1);
        email.setMemberId(memberId);

        var entity = createEntity(1);
        when(emailRepoMock.save(entity)).thenReturn(entity);
        when(emlMapMock.toEntity(email)).thenReturn(entity);
        when(emlMapMock.toDataObject(entity)).thenReturn(email);

        // execute
        var ret = svc.createItem(email);

        // verify
        assertNotNull(email);
        assertEquals(email, ret);
        verify(memRepoMock).getReferenceById(memberId);
        verify(emailRepoMock).save(entity);
        verify(emlMapMock).toEntity(email);
        verify(emlMapMock).toDataObject(entity);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void updateItem_returns_email() {
        // setup
        int id = 1;
        var entity = createEntity(id);
        var email = createDataObject(id);

        when(emailRepoMock.findByIdAndMemberId(id, id)).thenReturn(Optional.of(entity));

        when(emlMapMock.updateEntity(email, entity)).thenReturn(entity);
        when(emlMapMock.toDataObject(entity)).thenReturn(email);

        // execute
        var ret = svc.updateItem(id, email);

        // verify
        assertTrue(ret.isPresent());
        verify(emailRepoMock).findByIdAndMemberId(id, id);

        verify(emlMapMock).updateEntity(email, entity);
        verify(emlMapMock).toDataObject(entity);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }

    @Test
    public void deleteItem_deletes_email() {
        // setup
        int id = 1;
        doNothing().when(emailRepoMock).deleteById(id);

        // execute
        svc.deleteItem(id);

        //verify
        verify(emailRepoMock).deleteById(id);
        verifyNoMoreInteractions(emailRepoMock, emlMapMock, memRepoMock);
    }
    
    @Override
    protected EmailEntity createEntity(int flag) {
        return EmailEntity.builder()
                .id(flag)
                .email("email@emal.com")
                .emailType(EmailType.Home)
                .build();
    }

    @Override
    protected Email createDataObject(int flag) {
        return Email.builder()
                .id(flag)
                .memberId(flag)
                .email("email@emal.com")
                .emailType(EmailType.Home)
                .build();
    }
}
