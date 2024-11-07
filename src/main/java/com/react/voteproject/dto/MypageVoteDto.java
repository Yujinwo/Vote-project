package com.react.voteproject.dto;


import com.react.voteproject.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageVoteDto {

    private List<VoteResponseDto> vote;
    private int page;
    private Long total;
    private int pageSize;


    public static MypageVoteDto createMypageVoteDto(List<VoteResponseDto> vote,int page,Long total,int pageSize) {
        return MypageVoteDto.builder().vote(vote).page(page).pageSize(pageSize).total(total).build();
    }

}
