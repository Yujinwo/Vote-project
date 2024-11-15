package com.react.voteproject.dto;

import com.react.voteproject.entity.User;
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

    private String title;


    private String category;


    private int up;


    private Long commentCount;


    private String startDay;


    private String endDay;

    private List<VoteOptionDto> voteOptions;

    private int optionCountTotal;

    private UserDto user;

    private Long rank;


    public static HotVoteResponseDto createHotVoteResponseDto(Object[] vote,Long commentCount) {
        // 변환 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Vote hotvote = (Vote) vote[0];
        User user = (User) vote[1];
        Long rank = (Long) vote[2];
        // 원하는 형식으로 변환
        String startDay = hotvote.getStartDay().format(formatter);
        // 원하는 형식으로 변환
        String endDay = hotvote.getEndDay().format(formatter);

        int total = hotvote.getOptions().stream().mapToInt(o -> o.getCount()).sum();

        return HotVoteResponseDto.builder()
                .id(hotvote.getId())
                .rank(rank)
                .category(hotvote.getCategory())
                .up(hotvote.getUp())
                .commentCount(commentCount)
                .title(hotvote.getTitle())
                .startDay(startDay)
                .endDay(endDay)
                .optionCountTotal(total)
                .user(UserDto.createUserDto(user))
                .voteOptions(hotvote.getOptions().stream().map(o -> VoteOptionDto.createOptionDto(o,total)).collect(Collectors.toList())).build();

    }

}
