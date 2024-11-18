package com.react.voteproject.dto;

import com.react.voteproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatsDto {

    private String user_id;
    private String user_nick;
    private int vote;
    private int up;
    private int comment;
    private double rate;

    public static UserStatsDto createUserStatsDto(Object[] user, Double votingRateByUser) {
        Object[] objectUser = (Object[]) user[0];

        return  UserStatsDto.builder()
                    .user_id((String) objectUser[0])
                    .user_nick((String) objectUser[1])
                    .vote(((Long) objectUser[2]).intValue())
                    .up(((Long) objectUser[3]).intValue())
                    .comment(((Long) objectUser[4]).intValue())
                    .rate(votingRateByUser)
                    .build();

    }
}
