package com.smh.club.api.hateoas.integrationtests;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PageTestParams<T> {
    private Class<T> clazz;
    @Builder.Default
    private final Map<String, String> query = new HashMap<>();
    private String path;
    private int totalCount;
    private int pageNumber;
    @Builder.Default
    private int pageSize = 1;
    private String listNodeName;

    public String getListPath() {
        return "_embedded." + listNodeName;
    }

    public int getTotalPages() {
        return (totalCount / pageSize) +
            (totalCount % pageSize == 0 ? 0 : 1);
    }

    public static <T> PageTestParams<T> of(
        Class<T> clazz, Map<String, String> query, String path, int totalCount,
        int pageNumber, int pageSize, String listNodeMame) {

        return PageTestParams.<T>builder()
            .clazz(clazz)
            .query(query)
            .path(path)
            .totalCount(totalCount)
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .listNodeName(listNodeMame)
            .build();
    }
}
