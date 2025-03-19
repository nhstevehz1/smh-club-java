package com.smh.club.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountResponse {

    @JsonProperty("count")
    private final long count;

    private CountResponse(long count) {
        this.count = count;
    }

    public static CountResponse of(long count) {
        return new CountResponse(count);
    }
}
