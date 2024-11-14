package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.RenewalService;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
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
@WebMvcTest(RenewalControllerImpl.class)
public class RenewalControllerTests extends ControllerTests {

    @MockBean
    private RenewalService svc;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                    .set(Keys.JPA_ENABLED, true)
                    .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    public RenewalControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, "/renewals");
    }

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = Instancio.createList(RenewalDto.class);

        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<RenewalDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getRenewalListPage(any(PageParams.class))).thenReturn(response);

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

        verify(svc).getRenewalListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnRenewal() throws Exception {
        // setup
        var ret = Instancio.create(RenewalDto.class);
        when(svc.getRenewal(ret.getId())).thenReturn(Optional.of(ret));

        // execute
        mockMvc.perform(get(path + "/{id}", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(ret.getRenewalDate().toString()))
                .andExpect(jsonPath("$.renewal-year").value(ret.getRenewalYear()))
                .andDo(print());

        verify(svc).getRenewal(ret.getId());
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_renewalId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getRenewal(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getRenewal(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateRenewal() throws Exception {
        // setup
        var ret = Instancio.create(RenewalDto.class);
        var create = modelMapper.map(ret, CreateRenewalDto.class);
        when(svc.createRenewal(create)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post("/renewals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(ret)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(ret.getRenewalDate().toString()))
                .andExpect(jsonPath("$.renewal-year").value(ret.getRenewalYear()))
                .andDo(print());

        verify(svc).createRenewal(create);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldUpdateRenewal() throws Exception {
        //setup
        var ret = Instancio.create(RenewalDto.class);
        var update = modelMapper.map(ret, UpdateRenewalDto.class);
        when(svc.updateRenewal(ret.getId(), update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put(path + "/{id}", ret.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(ret)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.renewal-date").value(ret.getRenewalDate().toString()))
                .andExpect(jsonPath("$.renewal-year").value(ret.getRenewalYear()))
                .andDo(print());

        verify(svc).updateRenewal(ret.getId(), update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_renewal_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var update = Instancio.create(UpdateRenewalDto.class);
        when(svc.updateRenewal(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateRenewal(id, update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteRenewal()  throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteRenewal(id);

        // execute and verify
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deleteRenewal(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnRenewalCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getRenewalCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get(path + "/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getRenewalCount();
        verifyNoMoreInteractions(svc);
    }
}
