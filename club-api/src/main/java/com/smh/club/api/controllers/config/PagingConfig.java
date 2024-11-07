package com.smh.club.api.controllers.config;

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
    public final static String pageName = "page";
    public final static String sizeName = "size";
    public final static String sortDirName = "sortDir";
    public final static String sortName = "sort";
    public int pageSize;
    public int page;
    public Sort.Direction direction;
    public String sort;
}
