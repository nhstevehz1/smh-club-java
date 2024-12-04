package com.smh.club.api.hateoas.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.hateoas.models.AddressModel;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    protected <T> T sendValidCreate(T create, Class<T> clazz) throws JsonProcessingException {
        // perform post
        var result =
            given()
                .auth().none()
                .accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(create))
                .when()
                .post(path);

        var model = result.then().extract().body().as(AddressModel.class);
        var uri = "http://localhost" + path + "/" + model.getId();

        return result.then()
                .assertThat().status(HttpStatus.CREATED)
                .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
                .expect(jsonPath("$._links").exists())
                .expect(jsonPath("$._links.length()").value(3))
                .expect(jsonPath("$._links.self.href").value(uri))
                .expect(jsonPath("$._links.update.href").value(uri))
                .expect(jsonPath("$._links.delete.href").value(uri))
                .extract().body().as(clazz);

    }

    protected <T> void sendInvalidCreate(T create) throws JsonProcessingException {
        given()
            .auth().none()
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapper.writeValueAsString(create))
            .when()
            .post(path)
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .assertThat().contentType(ContentType.JSON)
            .expect(jsonPath("$.validation-errors").isNotEmpty())
            .expect(jsonPath("$.validation-errors.length()").value(1));
    }

    protected <T> T sendValidUpdate(int id, T update, Class<T> clazz) throws JsonProcessingException {
        var result =
            given()
                .auth().none()
                .accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam("id", id)
                .body(mapper.writeValueAsString(update))
                .when()
                .put(path + "/{id}");

        var model = result.then().extract().body().as(AddressModel.class);
        var uri = "http://localhost" + path + "/" + model.getId();

        return result.then()
            .assertThat().status(HttpStatus.OK)
            .assertThat().contentType(MediaTypes.HAL_JSON_VALUE)
            .expect(jsonPath("$._links").exists())
            .expect(jsonPath("$._links.length()").value(3))
            .expect(jsonPath("$._links.self.href").value(uri))
            .expect(jsonPath("$._links.update.href").value(uri))
            .expect(jsonPath("$._links.delete.href").value(uri))
            .extract().body().as(clazz);
    }

    protected <T> void sendInvalidUpdate(int id, T update) throws JsonProcessingException {
        given()
            .auth().none()
            .contentType(MediaType.APPLICATION_JSON)
            .pathParam("id", id)
            .body(mapper.writeValueAsString(update))
            .when()
            .put(path + "/{id}")
            .then()
            .assertThat().status(HttpStatus.BAD_REQUEST)
            .expect(jsonPath("$.validation-errors").isNotEmpty())
            .expect(jsonPath("$.validation-errors.length()").value(1));
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
