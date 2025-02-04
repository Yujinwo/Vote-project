package com.react.voteproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CacheRefreshTokenDto {
    private String refreshToken;
    private String ip;
    private String userAgent;
}
