package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.response.PageResponse;

import java.io.IOException;

public abstract class ControllerIntegrationTestsBase {

    protected ObjectMapper mapper;

    public ControllerIntegrationTestsBase(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> PageResponse<T> readPageResponse(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageResponse.class, contentClass);
        return mapper.readValue(json, type);
    }
}
