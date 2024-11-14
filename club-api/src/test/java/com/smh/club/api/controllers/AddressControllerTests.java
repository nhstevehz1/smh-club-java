package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.dto.update.UpdateAddressDto;
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
@WebMvcTest(AddressControllerImpl.class)
public class AddressControllerTests extends ControllerTests {

    @MockBean
    private AddressService svc;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0);

    @Autowired
    public AddressControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, "/addresses");
    }
    
    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = Instancio.createList(AddressDto.class);
        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<AddressDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getAddressListPage(any(PageParams.class))).thenReturn(response);

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

        verify(svc).getAddressListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnAddress() throws Exception {
        // setup
        var ret = Instancio.create(AddressDto.class);
        when(svc.getAddress(ret.getId())).thenReturn(Optional.of(ret));

        // execute
        mockMvc.perform(get(path + "/{id}", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.address1").value(ret.getAddress1()))
                .andExpect(jsonPath("$.address2").value(ret.getAddress2()))
                .andExpect(jsonPath("$.city").value(ret.getCity()))
                .andExpect(jsonPath("$.state").value(ret.getState()))
                .andExpect(jsonPath("$.zip").value(ret.getZip()))
                .andExpect(jsonPath("$.address-type").value(ret.getAddressType().getAddressTypeName()))
                .andDo(print());

        verify(svc).getAddress(ret.getId());
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_addressId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getAddress(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getAddress(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateAddress() throws Exception {
        // setup
        var ret = Instancio.create(AddressDto.class);
        var create = modelMapper.map(ret, CreateAddressDto.class);

        when(svc.createAddress(create)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.address1").value(ret.getAddress1()))
                .andExpect(jsonPath("$.address2").value(ret.getAddress2()))
                .andExpect(jsonPath("$.city").value(ret.getCity()))
                .andExpect(jsonPath("$.state").value(ret.getState()))
                .andExpect(jsonPath("$.zip").value(ret.getZip()))
                .andExpect(jsonPath("$.address-type").value(ret.getAddressType().getAddressTypeName()))
                .andDo(print());

        verify(svc).createAddress(create);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldUpdateAddress() throws Exception {
        // setup
        var ret = Instancio.create(AddressDto.class);
        var update = modelMapper.map(ret, UpdateAddressDto.class);
        when(svc.updateAddress(ret.getId(), update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put("/addresses/{id}", ret.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-id").value(ret.getMemberId()))
                .andExpect(jsonPath("$.address1").value(ret.getAddress1()))
                .andExpect(jsonPath("$.address2").value(ret.getAddress2()))
                .andExpect(jsonPath("$.city").value(ret.getCity()))
                .andExpect(jsonPath("$.state").value(ret.getState()))
                .andExpect(jsonPath("$.zip").value(ret.getZip()))
                .andExpect(jsonPath("$.address-type").value(ret.getAddressType().getAddressTypeName()))
                .andDo(print());

        verify(svc).updateAddress(ret.getId(), update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_address_should_return_badRequest() throws Exception {
        // setup
        var id = 12;
        var update = Instancio.create(UpdateAddressDto.class);
        when(svc.updateAddress(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateAddress(id, update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteAddress() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteAddress(id);

        // execute and verify
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void shouldReturnAddressCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getAddressCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get(path + "/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

    }
}
