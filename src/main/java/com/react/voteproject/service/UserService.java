package com.react.voteproject.service;


import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.UserJoinDto;
import com.react.voteproject.dto.UserLoginDto;
import com.react.voteproject.dto.UserStatsDto;
import com.react.voteproject.entity.User;
import com.react.voteproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> login(UserLoginDto userLoginDto) {
       Optional<User> user = userRepository.Login(userLoginDto.getUser_id(), userLoginDto.getUser_pw());
       return user;
    }

    @Transactional
    public Optional<User> join(UserJoinDto userJoinDto) {
        Optional<User> user = Optional.of(userRepository.save(userJoinDto.createUser()));
        return user;
    }
    @Transactional(readOnly = true)
    public Optional<User> findUserId(String userid) {
        return userRepository.findByuserId(userid);
    }

    public UserStatsDto getStats() {

        Long id = AuthContext.getAuth().getId();
        Optional<Object[]> statsByUser = userRepository.findStatsByUser(id);
        if(statsByUser.isPresent()){
            Double votingRateByUser = userRepository.findVotingRateByUser(id);
            UserStatsDto userStatsDto = UserStatsDto.createUserStatsDto(statsByUser.get(),votingRateByUser);
            return userStatsDto;

        }
        else {
            return new UserStatsDto();
        }


    }
}
