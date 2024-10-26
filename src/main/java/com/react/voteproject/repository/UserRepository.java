package com.react.voteproject.repository;


import com.react.voteproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u Where u.userId = :user_id and u.userPw = :user_pw")
    Optional<User> Login(@Param("user_id") String user_id, @Param("user_pw") String user_pw);

    Optional<User> findByuserId(String user_id);

}
