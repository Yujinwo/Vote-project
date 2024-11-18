package com.react.voteproject.dto;


import com.react.voteproject.entity.Vote;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponseDto {
    private Long id;
    private String title;
    private String category;
    private int up;
    private Long commentCount;
    private String startDay;
    private String endDay;
    private List<VoteOptionDto> voteOptions;
    private int optionCountTotal;
    private UserDto user;

    public static VoteResponseDto createVoteResponseDto(Vote vote,Long commentCount) {
        // 변환 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 원하는 형식으로 변환
        String startDay = vote.getStartDay().format(formatter);
        String endDay = vote.getEndDay().format(formatter);
        int optionCountTotal = vote.getOptions().stream().mapToInt(o -> o.getCount()).sum();

        return VoteResponseDto.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .category(vote.getCategory())
                .up(vote.getUp())
                .commentCount(commentCount)
                .startDay(startDay)
                .endDay(endDay)
                .optionCountTotal(optionCountTotal)
                .user(UserDto.createUserDto(vote.getUser()))
                .voteOptions(vote.getOptions().stream().map(o -> VoteOptionDto.createOptionDto(o,optionCountTotal))
                        .collect(Collectors.toList()))
                .build();

    }

}
