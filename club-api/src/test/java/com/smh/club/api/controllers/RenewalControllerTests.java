package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.models.Renewal;
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
@WebMvcTest(RenewalControllerImpl.class)
public class RenewalControllerTests extends ControllerTestBase<Renewal> {

    @MockBean
    private RenewalService svc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnRenewal() throws Exception {
        // setup
        var id = 12;
        var renewal = createDataObject(id);
        when(svc.getItem(id)).thenReturn(Optional.of(renewal));

        // execute
        mockMvc.perform(get("/renewals/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(renewal.getId()))
                .andExpect(jsonPath("$.member-id").value(renewal.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(renewal.getRenewalDate()))
                .andExpect(jsonPath("$.renewal-year").value(renewal.getRenewalYear()))
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateRenewal() throws Exception {
        // setup
        var id = 12;
        var renewal = createDataObject(id);
        when(svc.createItem(renewal)).thenReturn(renewal);

        // execute and verify
        mockMvc.perform(post("/renewals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(renewal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(renewal.getId()))
                .andExpect(jsonPath("$.member-id").value(renewal.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(renewal.getRenewalDate()))
                .andExpect(jsonPath("$.renewal-year").value(renewal.getRenewalYear()))
                .andDo(print());

        verify(svc).createItem(renewal);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldUpdateRenewal() throws Exception {
        var id = 12;
        var renewal = createDataObject(id);
        when(svc.updateItem(id, renewal)).thenReturn(Optional.of(renewal));

        // execute and verify
        mockMvc.perform(put("/renewals/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(renewal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(renewal.getId()))
                .andExpect(jsonPath("$.member-id").value(renewal.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(renewal.getRenewalDate()))
                .andExpect(jsonPath("$.renewal-year").value(renewal.getRenewalYear()))
                .andDo(print());

        verify(svc).updateItem(id, renewal);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteRenewal()  throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteItem(id);

        // execute and verify
        mockMvc.perform(delete("/renewals/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deleteItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnRenewalCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getItemCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get("/renewals/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getItemCount();
        verifyNoMoreInteractions(svc);
    }
    
    @Override
    protected Renewal createDataObject(int flag) {
        return Renewal.builder()
                .id(flag)
                .memberId(flag)
                .renewalYear("2024")
                .build();
    }
}
