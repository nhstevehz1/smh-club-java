package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.response.PageResponse;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class IntegrationTests {

    protected final ObjectMapper mapper;
    protected final MockMvc mockMvc;
    protected final String path;


    public IntegrationTests(MockMvc mockMvc, ObjectMapper mapper, String path) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.path = path;
    }

    protected Settings getSettings() {
        return Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0);
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

    protected <T> String executeGetListPageV2(
            Class<T> clazz, // the content time
            String path, // path to the endpoint
            MultiValueMap<String, String> valueMap, // page and sort parameters
            int count, // total number of expected elements in the database
            int pageSize, // the expected page size
            int pageNum // expected page number
            ) throws Exception {

        var pages = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        //var length = Math.min(count, pageSize);

        // perform get
        var ret = mockMvc.perform(get(path)
                        .params(valueMap)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.embedded").exists())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").value(pageSize))
                .andExpect(jsonPath("$.page.totalElements").value(count))
                .andExpect(jsonPath("$.page.totalPages").value(pages))
                .andExpect(jsonPath("$.page.number").value(pageNum))
                .andDo(print()).andReturn();

        return ret.getResponse().getContentAsString();
    }

    private <T> PageResponse<T> readPageResponse(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageResponse.class, contentClass);
        return mapper.readValue(json, type);
    }

    private <T> Page<T> readPage(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageImpl.class, contentClass);
        return mapper.readValue(json, type);
    }
}
