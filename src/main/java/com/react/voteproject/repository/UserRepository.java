package com.react.voteproject.repository;


import com.react.voteproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u Where u.userId = :user_id and u.userPw = :user_pw")
    Optional<User> Login(@Param("user_id") String user_id, @Param("user_pw") String user_pw);

    Optional<User> findByuserId(String user_id);

    @Query("select u.userId,u.userPw,COUNT(distinct v.id) AS vote_count, COUNT(distinct p.id) AS up_count, COUNT(distinct c.id) AS comment_count " +
            "from User u " +
            "LEFT JOIN Vote v ON u.id = v.user.id " +
            "LEFT JOIN Up p ON u.id = p.user.id " +
            "LEFT JOIN Comment c ON u.id = c.user.id " +
            "where u.id = :user_id ")
    Optional<Object[]> findStatsByUser(@Param("user_id") Long user_id);


    @Query("select ( COUNT(uv.user.id) / (Select Count(v) FROM Vote v) ) * 100 AS participation_rate from User u " +
            "LEFT JOIN UserVote uv ON u.id = uv.user.id " +
            "Where u.id = :user_id ")
    Double findVotingRateByUser(@Param("user_id") Long user_id);

}
