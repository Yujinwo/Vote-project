package com.react.voteproject.repository;

import com.react.voteproject.entity.Up;
import com.react.voteproject.entity.User;
import com.react.voteproject.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UpRepository extends JpaRepository<Up,Long> {




    Optional<Up> findByVoteAndUser(Vote vote , User user);
}
