package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.models.Address;
import com.smh.club.api.models.AddressType;
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
@WebMvcTest(AddressControllerImpl.class)
public class AddressControllerTests extends ControllerTestBase<Address> {
    @MockBean
    private AddressService svc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<Address>builder()
                .totalPages(100).totalCount(20)
                .items(createDataObjectList(5))
                .build();

        when(svc.getItemListPage(any(PageParams.class))).thenReturn(response);

        // execute and verify
        var ret = mockMvc.perform(get("/addresses")
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
    public void shouldReturnAddress() throws Exception {
        // setup
        var id = 12;
        var address = createDataObject(id);
        when(svc.getItem(id)).thenReturn(Optional.of(address));

        // execute
        mockMvc.perform(get("/addresses/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(address.getId()))
                .andExpect(jsonPath("$.member-id").value(address.getMemberId()))
                .andExpect(jsonPath("$.address1").value(address.getAddress1()))
                .andExpect(jsonPath("$.address2").value(address.getAddress2()))
                .andExpect(jsonPath("$.city").value(address.getCity()))
                .andExpect(jsonPath("$.state").value(address.getState()))
                .andExpect(jsonPath("$.zip").value(address.getZip()))
                .andExpect(jsonPath("$.address-type").value(address.getAddressType().getAddressName()))
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_addressId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getItem(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get("/addresses/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateAddress() throws Exception {
        // setup
        var id = 12;
        var address = createDataObject(id);
        when(svc.createItem(address)).thenReturn(address);

        // execute and verify
        mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(address.getId()))
                .andExpect(jsonPath("$.member-id").value(address.getMemberId()))
                .andExpect(jsonPath("$.address1").value(address.getAddress1()))
                .andExpect(jsonPath("$.address2").value(address.getAddress2()))
                .andExpect(jsonPath("$.city").value(address.getCity()))
                .andExpect(jsonPath("$.state").value(address.getState()))
                .andExpect(jsonPath("$.zip").value(address.getZip()))
                .andExpect(jsonPath("$.address-type").value(address.getAddressType().getAddressName()))
                .andDo(print());

        verify(svc).createItem(address);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldUpdateAddress() throws Exception {
        // setup
        var id = 10;
        var address = createDataObject(id);
        when(svc.updateItem(id, address)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put("/addresses/{id}", address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(address.getId()))
                .andExpect(jsonPath("$.member-id").value(address.getMemberId()))
                .andExpect(jsonPath("$.address1").value(address.getAddress1()))
                .andExpect(jsonPath("$.address2").value(address.getAddress2()))
                .andExpect(jsonPath("$.city").value(address.getCity()))
                .andExpect(jsonPath("$.state").value(address.getState()))
                .andExpect(jsonPath("$.zip").value(address.getZip()))
                .andExpect(jsonPath("$.address-type").value(address.getAddressType().getAddressName()))
                .andDo(print());

        verify(svc).updateItem(address.getId(), address);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_address_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var address = createDataObject(id);
        when(svc.updateItem(id, address)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put("/addresses/{id}", address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateItem(address.getId(), address);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteAddress() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteItem(id);

        // execute and verify
        mockMvc.perform(delete("/addresses/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void shouldReturnAddressCount() throws Exception {
        // setup
        var count = 20;
        when(svc.getItemCount()).thenReturn(CountResponse.of(count));

        // execute and verify
        mockMvc.perform(get("/addresses/count"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.count").value(20))
                .andDo(print());

    }

    @Override
    protected Address createDataObject(int flag) {
        return Address.builder()
                .id(flag)
                .memberId(flag)
                .address1("address1_" + flag)
                .address2("address2_" + flag)
                .city("city_" + flag)
                .state("state_" + flag)
                .zip("zip_" + flag)
                .addressType(AddressType.Other)
                .build();
    }
}
