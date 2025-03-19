package com.smh.club.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PagedDto<T> {

  public PagedDto(Page<T> page) {
    this.content = page.getContent();
    this.metadata = new PagedDtoMetadata(
        page.getSize(),
        page.getNumber(),
        page.getTotalPages(),
        page.getTotalElements(),
        page.isFirst(),
        page.hasPrevious(),
        page.hasNext(),
        page.isLast()
    );
  }

  @JsonProperty("_content")
  private final List<T> content;

  @JsonProperty("page")
  private final PagedDtoMetadata metadata;

  public static <T> PagedDto<T> of(Page<T> page) {
    return new PagedDto<>(page);
  }

  public record PagedDtoMetadata(
      int size,
      int number,
      int totalPages,
      long totalElements,
      boolean isFirst,
      boolean hasPrevious,
      boolean hasNext,
      boolean isLast) {}
}
