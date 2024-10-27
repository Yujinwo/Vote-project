package com.react.voteproject.dto;


import com.react.voteproject.entity.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteOptionDto {
    private Long id;
    private String content;
    private double rate;

    public static VoteOptionDto createOptionDto(VoteOption voteOption, int total) {
        double rate = ((double) voteOption.getCount() / total) * 100; // 비율 계산
        int percentage = (int) Math.floor(rate);
        return VoteOptionDto.builder()
                .id(voteOption.getId())
                .content(voteOption.getContent())
                .rate(percentage)
                .build();
    }
}
