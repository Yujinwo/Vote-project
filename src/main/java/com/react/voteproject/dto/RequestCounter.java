package com.react.voteproject.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Setter
public class RequestCounter {
    int requestCount;

    public RequestCounter(long startTime, int requestCount) {
        this.requestCount = requestCount;
    }


}
