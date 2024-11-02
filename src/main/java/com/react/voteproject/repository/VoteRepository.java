package com.react.voteproject.repository;

import com.react.voteproject.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
}
