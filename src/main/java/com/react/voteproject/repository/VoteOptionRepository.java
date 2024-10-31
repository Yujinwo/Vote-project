package com.react.voteproject.repository;


import com.react.voteproject.entity.Vote;
import com.react.voteproject.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption,Long> {


    List<VoteOption> findByvote(Vote vote);
}
