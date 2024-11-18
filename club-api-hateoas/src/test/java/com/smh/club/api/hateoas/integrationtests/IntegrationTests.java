package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

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

    protected <T> String executeGetListPage(
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

    /*private <T> Page<T> readPage(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageImpl.class, contentClass);
        return mapper.readValue(json, type);
    }*/
}
