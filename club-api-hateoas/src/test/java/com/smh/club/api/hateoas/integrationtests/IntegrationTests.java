package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
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

    protected <T> List<T> executeListPage(PageTestParams<T> testParams) {
        var result =
            given()
                .auth().none()
                .accept(MediaTypes.HAL_JSON)
                .queryParams(testParams.getQuery())
            .when()
                .get(testParams.getPath());

        result.then()
            .expect(status().isOk())
            .expect(jsonPath("page.size").value(testParams.getPageSize()))
            .expect(jsonPath("page.totalElements").value(testParams.getTotalCount()))
            .expect(jsonPath("page.totalPages").value(testParams.getTotalPages()))
            .expect(jsonPath("page.number").value(testParams.getPageNumber()));

        return result.getBody().jsonPath().getList(testParams.getListPath(), testParams.getClazz());
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
        private Comparator<M> model;

        public static <E, M> SortFields<E, M> of(Comparator<E> entity, Comparator<M> model) {
            return SortFields.<E, M>builder()
                .entity(entity)
                .model(model)
                .build();
        }
    }
}
