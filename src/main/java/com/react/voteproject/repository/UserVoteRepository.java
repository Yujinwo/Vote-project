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
    // 유저 투표 참여 현황 조회
    @Query("SELECT uv FROM UserVote uv WHERE uv.vote = :vote_id and uv.user = :user_id ")
    Optional<UserVote> findByVoteAndUser(@Param("vote_id") Vote vote_id, @Param("user_id") User user_id);

    // 유저 투표 참여 리스트 조회
    Page<UserVote> findByuser(PageRequest pageRequest, User user);
}
