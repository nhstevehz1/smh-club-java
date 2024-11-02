package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.models.Email;
import com.smh.club.api.models.EmailType;
import com.smh.club.api.response.CountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(EmailControllerImpl.class)
public class EmailControllerTests extends ControllerTestBase<Email> {
    @MockBean
    private EmailService svc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnEmail() throws Exception {
        // setup
        var id = 12;
        var email = createDataObject(id);
        when(svc.getItem(id)).thenReturn(Optional.of(email));

        // execute
        mockMvc.perform(get("/emails/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(email.getId()))
                .andExpect(jsonPath("$.member-id").value(email.getMemberId()))
                .andExpect(jsonPath("$.email").value(email.getEmail()))
                .andExpect(jsonPath("$.email-type").value(email.getEmailType().getEmailName()))
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateEmail() throws Exception {
        // setup
        var id = 12;
        var email = createDataObject(id);
        when(svc.createItem(email)).thenReturn(email);

        // execute and verify
        mockMvc.perform(post("/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(email.getId()))
                .andExpect(jsonPath("$.member-id").value(email.getMemberId()))
                .andExpect(jsonPath("$.email").value(email.getEmail()))
                .andExpect(jsonPath("$.email-type").value(email.getEmailType().getEmailName()))
                .andDo(print());

        verify(svc).createItem(email);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldUpdateEmail() throws Exception {
        // setup
        var id = 12;
        var email = createDataObject(id);
        when(svc.updateItem(id, email)).thenReturn(Optional.of(email));

        // execute and verify
        mockMvc.perform(put("/emails/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(email.getId()))
                .andExpect(jsonPath("$.member-id").value(email.getMemberId()))
                .andExpect(jsonPath("$.email").value(email.getEmail()))
                .andExpect(jsonPath("$.email-type").value(email.getEmailType().getEmailName()))
                .andDo(print());

        verify(svc).updateItem(id, email);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteEmail() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteItem(id);

        // execute and verify
        mockMvc.perform(delete("/emails/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deleteItem(id);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldReturnEmailCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getItemCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get("/emails/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getItemCount();
        verifyNoMoreInteractions(svc);
    }

    @Override
    protected Email createDataObject(int flag) {
        return Email.builder()
                .id(flag)
                .memberId(flag)
                .email("something@test.com")
                .emailType(EmailType.Work)
                .build();
    }
}
