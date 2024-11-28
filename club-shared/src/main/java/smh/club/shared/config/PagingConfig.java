package smh.club.shared.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;

/**
 * Custom annotations for settable paging properties.
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("request.paging")
public class PagingConfig {
    public final static String PAGE_NAME = "page";
    public final static String SIZE_NAME = "size";
    //TODO: Remove after pageable implementation
    public final static String DIRECTION_NAME = "sortDir";
    public final static String SORT_NAME = "sort";
    public int pageSize = 10;
    public int page = 0;
    public Sort.Direction direction = Sort.Direction.ASC;
}
