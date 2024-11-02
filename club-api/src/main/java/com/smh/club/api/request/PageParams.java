package com.smh.club.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageParams {

    @Builder.Default
    @JsonProperty("page")
    int pageNumber = 0;

    @Builder.Default
    @JsonProperty("size")
    int pageSize = 10;

    @Builder.Default
    @JsonProperty("sort")
    String sortColumn = "default";

    @Builder.Default
    @JsonProperty("order")
    public Sort.Direction sortDirection = Sort.Direction.ASC;

    public static PageParams getDefault() {
        return new PageParams();
    }
}
