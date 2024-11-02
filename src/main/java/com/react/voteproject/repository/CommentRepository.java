package com.react.voteproject.repository;

import com.react.voteproject.entity.Comment;
import com.react.voteproject.entity.Vote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c where vote = :vote_id")
    List<Comment> findByVote(@Param("vote_id") Vote vote_id);
    @Query("select c from Comment c where vote = :vote_id")
    Slice<Comment> findSliceByVote(@Param("vote_id") Vote vote_id, Pageable pageable);

    @Query("select count(c) from Comment c where vote = :vote_id")
    long countCommentsByVote(@Param("vote_id") Vote vote_id);

}
