package com.react.voteproject.repository;


import com.react.voteproject.entity.Vote;
import com.react.voteproject.entity.VoteOption;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption,Long> {

    // 투표 선택지 리스트 조회
    List<VoteOption> findByvote(Vote vote);

    // 투표 선택지 조회 비관적 Lock 잠금
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<VoteOption> findById(Long id);

}
