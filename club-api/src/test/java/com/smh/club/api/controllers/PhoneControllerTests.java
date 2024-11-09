package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.PhoneService;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.smh.club.api.helpers.datacreators.PhoneCreators.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@WebMvcTest(PhoneControllerImpl.class)
public class PhoneControllerTests extends ControllerTests {
    @MockBean
    private PhoneService svc;

    @Autowired
    protected PhoneControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, new ModelMapper(), "/phones");
    }

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = genPhoneDtoList(5);

        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<PhoneDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getItemListPage(any(PageParams.class))).thenReturn(response);

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

        verify(svc).getItemListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnPhone() throws Exception {
        // setup
        var id = 12;
        var phone = genPhoneDto(id);
        when(svc.getItem(id)).thenReturn(Optional.of(phone));

        // execute
        mockMvc.perform(get( path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(phone.getId()))
                .andExpect(jsonPath("$.member-id").value(phone.getMemberId()))
                .andExpect(jsonPath("$.phone-number").value(phone.getPhoneNum()))
                .andExpect(jsonPath("$.phone-type").value(phone.getPhoneType().getPhoneTypeName()))
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_phoneId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getItem(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreatePhone() throws Exception {
        // setup
        var id = 12;
        var create  = genCreatePhoneDto(id);
        var ret = genPhoneDto(id);
        when(svc.createItem(create)).thenReturn(ret);

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

        verify(svc).createItem(create);
        verifyNoMoreInteractions(svc);
    }


    @Test
    public void shouldUpdatePhone() throws Exception {
    // setup
        var id = 12;
        var ret = genPhoneDto(id);
        var update = genUpdatePhoneDto(id);
        when(svc.updateItem(id, update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put("/phones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.phone-number").value(ret.getPhoneNum()))
                .andExpect(jsonPath("$.phone-type").value(ret.getPhoneType().getPhoneTypeName()))
                .andDo(print());

        verify(svc).updateItem(id, update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_phone_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var update = genUpdatePhoneDto(id);
        when(svc.updateItem(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateItem(id, update);
        verifyNoMoreInteractions(svc);
    }
    
    @Test
    public void shouldDeletePhone() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteItem(id);

        // execute and verify
        mockMvc.perform(delete("/phones/{id}", id))
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
        mockMvc.perform(get("/phones/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

        verify(svc).getItemCount();
        verifyNoMoreInteractions(svc);
    }
}
