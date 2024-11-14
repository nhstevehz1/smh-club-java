package com.smh.club.api.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.controllers.ControllerTests;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@WebMvcTest(EmailControllerImpl.class)
public class EmailControllerTests extends ControllerTests {

    @MockBean
    private EmailService svc;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                    .set(Keys.JPA_ENABLED, true)
                    .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    EmailControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, "/api/v1/emails");
    }

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = Instancio.createList(EmailDto.class);
        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<EmailDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getEmailListPage(any(PageParams.class))).thenReturn(response);

        // execute and verify
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.total-count").value(response.getTotalCount()))
                .andExpect(jsonPath("$.items.length()").value(response.getItems().size()))
                .andDo(print());

        verify(svc).getEmailListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnEmail() throws Exception {
        // setup
        var ret = Instancio.create(EmailDto.class);
        when(svc.getEmail(ret.getId())).thenReturn(Optional.of(ret));

        // execute
        mockMvc.perform(get(path + "/{id}", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.email").value(ret.getEmail()))
                .andExpect(jsonPath("$.email-type").value(ret.getEmailType().getEmailTypeName()))
                .andDo(print());

        verify(svc).getEmail(ret.getId());
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_emailId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getEmail(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getEmail(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreate() throws Exception {
        // setup
        var ret = Instancio.create(EmailDto.class);
        var create = modelMapper.map(ret, EmailDto.class);
        when(svc.createEmail(create)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.email").value(ret.getEmail()))
                .andExpect(jsonPath("$.email-type").value(ret.getEmailType().getEmailTypeName()))
                .andDo(print());

        verify(svc).createEmail(create);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldUpdateEmail() throws Exception {
        // setup
        var ret = Instancio.create(EmailDto.class);
        var update = modelMapper.map(ret, EmailDto.class);
        when(svc.updateEmail(ret.getId(), update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put(path + "/{id}", ret.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(ret)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.email").value(ret.getEmail()))
                .andExpect(jsonPath("$.email-type").value(ret.getEmailType().getEmailTypeName()))
                .andDo(print());

        verify(svc).updateEmail(ret.getId(), update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_email_should_return_badRequest() throws Exception {
        // setup
        var id = 15;
        var update = Instancio.create(EmailDto.class);
        when(svc.updateEmail(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateEmail(id, update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDelete() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteEmail(id);

        // execute and verify
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deleteEmail(id);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldReturnEmailCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getEmailCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get(path + "/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getEmailCount();
        verifyNoMoreInteractions(svc);
    }
}
