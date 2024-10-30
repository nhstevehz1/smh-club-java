package com.smh.club.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
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
