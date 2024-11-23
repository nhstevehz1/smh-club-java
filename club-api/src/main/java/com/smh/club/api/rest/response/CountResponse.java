package com.smh.club.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountResponse {

    @JsonProperty("count")
    private long count;

    private CountResponse(long count) {
        this.count = count;
    }

    public static CountResponse of(long count) {
        return new CountResponse(count);
    }
}
