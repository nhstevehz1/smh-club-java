package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.data.dto.RenewalDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.junit.jupiter.api.Test;
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
@WebMvcTest(RenewalControllerImpl.class)
public class RenewalControllerTests extends ControllerTestBase<RenewalDto> {

    @MockBean
    private RenewalService svc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<RenewalDto>builder()
                .totalPages(100).totalCount(20)
                .items(createDataObjectList(5))
                .build();

        when(svc.getItemListPage(any(PageParams.class))).thenReturn(response);

        // execute and verify
        mockMvc.perform(get("/renewals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.total-count").value(response.getTotalCount()))
                .andExpect(jsonPath("$.items.length()").value(response.getItems().size()))
                .andDo(print());

        verify(svc).getItemListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

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
    public void shouldReturnNotFound_when_renewalId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getItem(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get("/renewals/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
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
    public void update_renewal_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var renewal = createDataObject(id);
        when(svc.updateItem(id, renewal)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put("/renewals/{id}", renewal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(renewal)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateItem(renewal.getId(), renewal);
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
    protected RenewalDto createDataObject(int flag) {
        return RenewalDto.builder()
                .id(flag)
                .memberId(flag)
                .renewalYear("2024")
                .build();
    }
}
