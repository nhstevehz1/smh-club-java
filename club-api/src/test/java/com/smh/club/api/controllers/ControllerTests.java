package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.data.config.MapperConfig;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTests {
    protected final MockMvc mockMvc;
    protected final ObjectMapper objMapper;
    protected final ModelMapper modelMapper = new  MapperConfig().createModelMapper();
    protected final String path;

    public ControllerTests(MockMvc mockMvc, ObjectMapper objMapper, String path) {
        this.mockMvc = mockMvc;
        this.objMapper = objMapper;
        this.path = path;
    }
}
