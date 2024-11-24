package com.smh.club.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Contains metadata for
 * @param <T>
 */
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

    /**
     * Creates a PageResponse of type T.
     * @param page A {@link Page}.
     * @return A {@link PageResponse}.
     */
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
