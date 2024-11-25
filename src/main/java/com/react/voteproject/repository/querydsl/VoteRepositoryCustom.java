package com.react.voteproject.repository.querydsl;

import com.react.voteproject.dto.VoteWithCommentCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoteRepositoryCustom {

    // My페이지 작성한 투표 조회
    Page<VoteWithCommentCountDTO> findVotesWithCommentCount(Pageable pageable,String user_id);

    // My페이지 참여한 투표 조회
    Page<VoteWithCommentCountDTO> findUserVotesWithCommentCount(Pageable pageable,String user_id);

    // My페이지 좋아요한 투표 조회
    Page<VoteWithCommentCountDTO> findUpVotesWithCommentCount(Pageable pageable,String user_id);

    // My페이지 북마크한 투표 조회
    Page<VoteWithCommentCountDTO> findBookmarkVotesWithCommentCount(Pageable pageable,String user_id);


}
