package com.smh.club.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageResponse<T> {

    @JsonProperty("total-pages")
    private long totalPages;

    @JsonProperty("total-count")
    private long totalCount;

    @JsonProperty("items")
    private List<T> items;
}
