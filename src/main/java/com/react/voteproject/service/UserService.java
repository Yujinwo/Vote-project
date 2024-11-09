package com.react.voteproject.service;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.UserJoinDto;
import com.react.voteproject.dto.UserLoginDto;
import com.react.voteproject.dto.UserStatsDto;
import com.react.voteproject.dto.UserVoteStatsDto;
import com.react.voteproject.entity.User;
import com.react.voteproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public UserStatsDto getUserStats() {

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

    public List<UserVoteStatsDto> getVoteStats(String category, String day) {
        if(category == null) {
            List<Object[]> userRankingWithActivityToday = new ArrayList<>();
            if(day.equals("Thisyear")) {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisYear();
            }
            else if(day.equals("Thismonth")) {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisMonth();
            }
            else {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityToday();
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }
        else {
            Boolean checkCategory = category_enum.fromCode(category);
            if(!checkCategory){
                return new ArrayList<>();
            }
            List<Object[]> userRankingWithActivityToday = new ArrayList<>();
            if(day.equals("Thisyear")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisYear(category);

            }
            else if(day.equals("Thismonth")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisMonth(category);
            }
            else {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityToday(category);
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }

    }
}
