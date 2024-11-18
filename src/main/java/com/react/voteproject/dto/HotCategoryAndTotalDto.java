package com.react.voteproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotCategoryAndTotalDto {

    private String category;
    private Long total;


    public static HotCategoryAndTotalDto createHotCategoryAndTotalDto(Object[] object) {

        Object[] objectSummary = (Object[]) object[0];
        return HotCategoryAndTotalDto.builder()
                .category((String) objectSummary[0])
                .total((Long) objectSummary[2])
                .build();
    }
}
