package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.response.PageResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class IntegrationTestsBase {

    protected ObjectMapper mapper;
    protected MockMvc mockMvc;

    public IntegrationTestsBase(MockMvc mockMvc, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    protected <T> List<T> executeGetListPage(
            Class<T> clazz, String path, MultiValueMap<String,
            String> valueMap, int count, int pageSize) throws Exception {
        var pages = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        var length = Math.min(count, pageSize);

        // perform get
        var ret = mockMvc.perform(get(path)
                        .params(valueMap)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        return readPageResponse(ret.getResponse().getContentAsString(), clazz).getItems();
    }

    private <T> PageResponse<T> readPageResponse(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageResponse.class, contentClass);
        return mapper.readValue(json, type);
    }
}
