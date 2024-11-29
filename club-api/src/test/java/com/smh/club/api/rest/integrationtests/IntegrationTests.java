package com.smh.club.api.rest.integrationtests;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.rest.response.PageResponse;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import smh.club.shared.api.config.PagingConfig;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
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
        configure();
    }

    // TODO: remove after integration test fixes
    protected Settings getSettings() {
        return Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                .set(Keys.JPA_ENABLED, true)
                .set(Keys.COLLECTION_MAX_SIZE, 0);
    }

    // TODO: remove when refactor is complete
    protected <T> List<T> executeGetListPage(
            Class<T> clazz, String path, MultiValueMap<String,
            String> valueMap, int count, int pageSize) throws Exception {

        var pageNumber = valueMap.getFirst(PagingConfig.PAGE_NAME);
        pageNumber = pageNumber != null ? pageNumber : "0";

        var pages = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        var length = Math.min(count, pageSize);

        // perform get
        var ret = mockMvc.perform(get(path)
                        .params(valueMap)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
/*                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.page-size").value(pageSize))
                .andExpect(jsonPath("$.page-number").value(pageNumber))
                .andExpect(jsonPath("$.content.length()").value(length))*/
                .andDo(print()).andReturn();

        return readPageResponse(ret.getResponse().getContentAsString(), clazz).getContent();
    }

    protected <T> List<T> executeListPage(PageTestParams<T> testParams) {
        var result =
            given()
                .auth().none()
                .accept(MediaType.APPLICATION_JSON)
                .queryParams(testParams.getQuery())
                .when()
                .get(testParams.getPath());

        result.then()
            .expect(status().isOk())
            .expect(jsonPath("page.size").value(testParams.getPageSize()))
            .expect(jsonPath("page.totalElements").value(testParams.getTotalCount()))
            .expect(jsonPath("page.totalPages").value(testParams.getTotalPages()))
            .expect(jsonPath("page.number").value(testParams.getPageNumber()))
            .expect(jsonPath("page.isFirst").value(testParams.isFirst()))
            .expect(jsonPath("page.hasPrevious").value(testParams.hasPrevious()))
            .expect(jsonPath("page.hasNext").value(testParams.hasNext()))
            .expect(jsonPath("page.isLast").value(testParams.isLast()));

        return result.getBody().jsonPath().getList(testParams.getListPath(), testParams.getClazz());
    }

    private <T> PageResponse<T> readPageResponse(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageResponse.class, contentClass);
        return mapper.readValue(json, type);
    }

    private void configure() {
        // setup RestAssured to use the MockMvc Context
        RestAssuredMockMvc.mockMvc(mockMvc);

        // Configure RestAssured to use the injected Object mapper.
        RestAssuredMockMvc.config =
            RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> mapper));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Builder
    public static class SortFields<E, M> {
        private Comparator<E> entity;
        private Comparator<M> dto;

        public static <E, M> SortFields<E, M> of(Comparator<E> entity, Comparator<M> dto) {
            return SortFields.<E, M>builder()
                .entity(entity)
                .dto(dto)
                .build();
        }
    }
}
