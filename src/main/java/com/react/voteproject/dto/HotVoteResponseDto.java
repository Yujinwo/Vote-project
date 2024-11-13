package com.react.voteproject.dto;

import com.react.voteproject.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotVoteResponseDto {
    private Long id;

    private Long rank;

    private String title;

    private String startDay;

    private String endDay;

    private List<VoteOptionDto> voteOptions;

    public static HotVoteResponseDto createHotVoteResponseDto(Object[] vote) {
        //Object[] objectHotVote = (Object[]) vote[0];
        // 변환 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Vote hotvote = (Vote) vote[0];
        Long rank = (Long) vote[1];
        // 원하는 형식으로 변환
        String startDay = hotvote.getStartDay().format(formatter);
        // 원하는 형식으로 변환
        String endDay = hotvote.getEndDay().format(formatter);

        int total = hotvote.getOptions().stream().mapToInt(o -> o.getCount()).sum();

        return HotVoteResponseDto.builder()
                .id(hotvote.getId())
                .rank(rank)
                .title(hotvote.getTitle())
                .startDay(startDay).endDay(endDay)
                .voteOptions(hotvote.getOptions().stream().map(o -> VoteOptionDto.createOptionDto(o,total)).collect(Collectors.toList())).build();

    }

}
