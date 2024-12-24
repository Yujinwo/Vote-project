package com.react.voteproject.repository;

import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    // 투표 댓글 리스트 조회
    @Query("select c from Comment c where vote = :vote_id")
    Slice<Comment> findPageByVote(@Param("vote_id") Vote vote_id, Pageable pageable);

    // 투표 댓글 Count 조회
    @Query("select count(c) from Comment c where vote = :vote_id")
    Long findCommentcountByVote(@Param("vote_id") Vote vote_id);

}
