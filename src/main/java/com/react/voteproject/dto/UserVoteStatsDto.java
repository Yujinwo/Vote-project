package com.react.voteproject.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVoteStatsDto {
    private int rank;
    private String user_id;
    private String user_nick;
    private double rate;
    private int vote;
    private int up;

    private int comment;
    private String category;

    public static UserVoteStatsDto createUserVoteStatsDto(Object[] userVote) {



        Long voteValue = (Long) userVote[2];
        Long upValue = (Long) userVote[3];
        Long commentValue = (Long) userVote[4];
        double rateCount = 0;
        if(userVote[6] != null) {
            BigDecimal rateValue = (BigDecimal) userVote[6];
            rateCount = rateValue != null ? rateValue.doubleValue() : 0; // 또는 적절한 기본값
        }
        else {
            Long rateValue = (Long) userVote[6];
            rateCount = rateValue != null ? rateValue.doubleValue() : 0; // 또는 적절한 기본값
        }

        int voteCount = voteValue != null ? voteValue.intValue() : 0; // 또는 적절한 기본값
        int upCount = upValue != null ? upValue.intValue() : 0; // 또는 적절한 기본값
        int commentCount = commentValue != null ? commentValue.intValue() : 0; // 또는 적절한 기본값
        int rank = userVote.length == 9 ? ((Long) userVote[8]).intValue() : ((Long) userVote[7]).intValue();
        String category = userVote.length == 9 ? (String) userVote[7] : null;
        return  UserVoteStatsDto.builder()
                .user_id((String) userVote[0])
                .user_nick((String) userVote[1])
                .category(category)
                .vote(voteCount)
                .up(upCount )
                .comment(commentCount)
                .rate(rateCount)
                .rank(rank)
                .build();

    }
}
