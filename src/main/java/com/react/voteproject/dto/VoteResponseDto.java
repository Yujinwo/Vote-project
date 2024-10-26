package com.react.voteproject.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponseDto {


    private String title;


    private String category;


    private int up;


    private int commentCount;


    private LocalDateTime startDay;


    private LocalDateTime endDay;
    List<String> voteoptions;
}
