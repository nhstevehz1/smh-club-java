package com.smh.club.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PageResponse<T> {

    @JsonProperty("page-size")
    private int pageSize;

    @JsonProperty("page-number")
    private int pageNumber;

    @JsonProperty("total-pages")
    private int totalPages;

    @JsonProperty("total-count")
    private long totalCount;

    @JsonProperty("content")
    private List<T> content;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .pageNumber(page.getNumber())
                .build();
    }
}
