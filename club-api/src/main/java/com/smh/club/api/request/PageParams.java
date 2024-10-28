package com.smh.club.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageParams {
    @JsonProperty("page")
    int pageNumber;

    @JsonProperty("size")
    int pageSize;

    @JsonProperty("sort")
    String sortColumn;

    @JsonProperty("order")
    public Sort.Direction sortDirection;

    public static PageParams getDefault() {
        return new PageParams(0, 5, "default", Sort.Direction.ASC);
    }
}
