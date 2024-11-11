package com.smh.club.api.configuration;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Builder
public class ColumnSortMap {

    private final Map<String, String> map;
    private final String defaultValue;

    public String getColName(String sort) {

        return map.getOrDefault(sort, map.get(defaultValue));
    }
}
