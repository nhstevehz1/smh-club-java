package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTests {
    protected final MockMvc mockMvc;
    protected final ObjectMapper objMapper;
    protected final ModelMapper modelMapper;
    protected final String path;

    protected ControllerTests(MockMvc mockMvc, ObjectMapper objMapper, ModelMapper modelMapper, String path) {
        this.mockMvc = mockMvc;
        this.objMapper = objMapper;
        this.modelMapper = modelMapper;
        this.path = path;
    }
}
