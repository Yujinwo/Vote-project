package com.react.voteproject.repository;


import com.react.voteproject.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVoteRepository extends JpaRepository<UserVote,Long> {

    @Query("SELECT uv FROM UserVote uv WHERE uv.vote = :vote_id")
    Optional<UserVote> findByVoteId(@Param("vote_id") Vote vote_id);


    @Modifying
    @Query("DELETE FROM UserVote uv WHERE uv.vote = :vote_id")
    int deleteByVoteID(@Param("vote_id") Vote vote_id);


    @Query("SELECT uv FROM UserVote uv WHERE uv.voteOption = :vote_option_id")
    Optional<UserVote> findByVoteOptionId(@Param("vote_option_id") VoteOption vote_option_id);

    Page<UserVote> findByuser(PageRequest pageRequest, User user);
}
