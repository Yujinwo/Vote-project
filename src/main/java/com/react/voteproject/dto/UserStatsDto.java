package com.react.voteproject.dto;

import com.react.voteproject.context.AuthContext;
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
        Object[] objectUser = ((Object[]) user[0]) ;
        User user1 = AuthContext.getAuth();

        int vote_count = objectUser[0] != null ? ((Long) objectUser[0]).intValue() : 0;
        int up_count = objectUser[1] != null ? ((Long) objectUser[1]).intValue() : 0;
        int comment_count = objectUser[2] != null ? ((Long) objectUser[2]).intValue() : 0;



        return  UserStatsDto.builder()
                    .user_id(user1.getUserId())
                    .user_nick(user1.getUserNick())
                    .vote(vote_count)
                    .up(up_count)
                    .comment(comment_count)
                    .rate(votingRateByUser)
                    .build();

    }
}
