package com.smh.club.api.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("request.paging")
public class PagingConfig {
    public final static String PAGE_NAME = "page";
    public final static String SIZE_NAME = "size";
    public final static String DIRECTION_NAME = "sortDir";
    public final static String SORT_NAME = "sort";
    public int pageSize = 10;
    public int page = 0;
    public Sort.Direction direction = Sort.Direction.ASC;
}
