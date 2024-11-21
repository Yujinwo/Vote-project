package com.react.voteproject.service;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.User;
import com.react.voteproject.jwt.JwtProvider;
import com.react.voteproject.jwt.RefreshToken;
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
    private final JwtProvider jwtProvider;

    // 로그인
    @Transactional(readOnly = true)
    public LoginResponseDTO login(UserLoginDto userLoginDto) {
       Optional<User> user = userRepository.Login(userLoginDto.getUser_id(), userLoginDto.getUser_pw());
       if(user.isPresent()) {
           User userInfo = user.get();
           AuthContext.setAuth(user.get());
           // jwt 토큰 생성
           String accessToken = jwtProvider.generateAccessToken(userInfo.getId());

           // 기존에 가지고 있는 사용자의 refresh token 제거
           RefreshToken.removeUserRefreshToken(userInfo.getId());

           // refresh token 생성 후 저장
           String refreshToken = jwtProvider.generateRefreshToken(userInfo.getId());
           RefreshToken.putRefreshToken(refreshToken, userInfo.getId());

           return LoginResponseDTO.builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build();

       }

      return new LoginResponseDTO();
    }
    // 회원가입
    @Transactional
    public Optional<User> join(UserJoinDto userJoinDto) {
        Optional<User> user = Optional.of(userRepository.save(userJoinDto.createUser()));
        return user;
    }
    // id 조회
    @Transactional(readOnly = true)
    public Optional<User> findUserId(String userid) {
        return userRepository.findByuserId(userid);
    }
    // My페이지 유저 통계 데이터 조회
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
    // 유저 투표 참여율 순위 조회
    public List<UserVoteStatsDto> getVoteStats(String category, String day) {
        List<Object[]> userRankingWithActivityToday = new ArrayList<>();
        if(category == null) {
            if(day.equals("Thisyear")) {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisYear(null,100);
            }
            else if(day.equals("Thismonth")) {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisMonth(null,100);
            }
            else {
               userRankingWithActivityToday = userRepository.findUserRankingWithActivityToday(null,100);
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }
        else {
            Boolean checkCategory = category_enum.fromCode(category);
            if(!checkCategory){
                return new ArrayList<>();
            }
            if(day.equals("Thisyear")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisYear(category,null,100);

            }
            else if(day.equals("Thismonth")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisMonth(category,null,100);
            }
            else {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityToday(category,null,100);
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }
    }
    // 유저 투표 참여율 순위 id 기준 조회
    public List<UserVoteStatsDto> searchVoteStats(String userId, String category, String day) {
        List<Object[]> userRankingWithActivityToday = new ArrayList<>();
        if(category == null) {
            if(day.equals("Thisyear")) {
                userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisYear(userId,1000);
            }
            else if(day.equals("Thismonth")) {
                userRankingWithActivityToday = userRepository.findUserRankingWithActivityThisMonth(userId,1000);
            }
            else {
                userRankingWithActivityToday = userRepository.findUserRankingWithActivityToday(userId,1000);
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }
        else {
            Boolean checkCategory = category_enum.fromCode(category);
            if(!checkCategory){
                return new ArrayList<>();
            }
            if(day.equals("Thisyear")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisYear(category,userId,1000);

            }
            else if(day.equals("Thismonth")) {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityThisMonth(category,userId,1000);
            }
            else {
                userRankingWithActivityToday = userRepository.findUserCategoryRankingWithActivityToday(category,userId,1000);
            }
            List<UserVoteStatsDto> userVoteStatsDtos = userRankingWithActivityToday.stream().map(UserVoteStatsDto::createUserVoteStatsDto).collect(Collectors.toList());
            return userVoteStatsDtos;
        }

    }
}
