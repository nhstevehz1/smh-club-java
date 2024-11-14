package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
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
@WebMvcTest(PhoneControllerImpl.class)
public class PhoneControllerTests extends ControllerTests {

    @MockBean
    private PhoneService svc;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                    .set(Keys.JPA_ENABLED, true)
                    .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    protected PhoneControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, "/phones");
    }

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = Instancio.createList(PhoneDto.class);

        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<PhoneDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getPhoneListPage(any(PageParams.class))).thenReturn(response);

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

        verify(svc).getPhoneListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnPhone() throws Exception {
        // setup
        var ret = Instancio.create(PhoneDto.class);
        when(svc.getPhone(ret.getId())).thenReturn(Optional.of(ret));

        // execute
        mockMvc.perform(get( path + "/{id}", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.phone-number").value(ret.getPhoneNum()))
                .andExpect(jsonPath("$.phone-type").value(ret.getPhoneType().getPhoneTypeName()))
                .andDo(print());

        verify(svc).getPhone(ret.getId());
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_phoneId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getPhone(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getPhone(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreatePhone() throws Exception {
        // setup
        var ret  = Instancio.create(PhoneDto.class);
        var create = modelMapper.map(ret, CreatePhoneDto.class);
        when(svc.createPhone(create)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post("/phones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.phone-number").value(ret.getPhoneNum()))
                .andExpect(jsonPath("$.phone-type").value(ret.getPhoneType().getPhoneTypeName()))
                .andDo(print());

        verify(svc).createPhone(create);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldUpdatePhone() throws Exception {
    // setup
        var ret = Instancio.create(PhoneDto.class);
        var update = modelMapper.map(ret, CreatePhoneDto.class);
        when(svc.updatePhone(ret.getId(), update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put("/phones/{id}", ret.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.phone-number").value(ret.getPhoneNum()))
                .andExpect(jsonPath("$.phone-type").value(ret.getPhoneType().getPhoneTypeName()))
                .andDo(print());

        verify(svc).updatePhone(ret.getId(), update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_phone_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var update = Instancio.create(CreatePhoneDto.class);
        when(svc.updatePhone(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updatePhone(id, update);
        verifyNoMoreInteractions(svc);
    }
    
    @Test
    public void shouldDeletePhone() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deletePhone(id);

        // execute and verify
        mockMvc.perform(delete("/phones/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deletePhone(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnEmailCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getPhoneCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get("/phones/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getPhoneCount();
        verifyNoMoreInteractions(svc);
    }
}
