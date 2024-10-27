package com.react.voteproject.repository;


import com.react.voteproject.entity.User;
import com.react.voteproject.entity.UserVote;
import com.react.voteproject.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVoteRepository extends JpaRepository<UserVote,Long> {

    @Query("SELECT uv FROM UserVote uv WHERE uv.user = :user_id")
    Optional<UserVote> findByuserID(@Param("user_id") User user_id);


    @Modifying
    @Query("DELETE FROM UserVote uv WHERE uv.user = :user_id")
    int deleteByuserID(@Param("user_id") User user_id);


    @Query("SELECT uv FROM UserVote uv WHERE uv.voteOption = :vote_option_id")
    Optional<UserVote> findByVoteOptionId(@Param("vote_option_id") VoteOption vote_option_id);
}
