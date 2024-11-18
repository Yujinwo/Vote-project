package com.react.voteproject.service;


import com.react.voteproject.dto.MypageVoteDto;
import com.react.voteproject.dto.VoteResponseDto;
import com.react.voteproject.entity.*;
import com.react.voteproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final VoteRepository voteRepository;
    private final UpRepository upRepository;
    private final UserVoteRepository userVoteRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    // 유저가 작성한 투표 조회
    public MypageVoteDto findVotes(Pageable pageable, User user ){
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<Vote> pageVotes =  voteRepository.findByuser(pageRequest,user);
        List<VoteResponseDto> mypageVotes = pageVotes.stream().map(v -> VoteResponseDto.createVoteResponseDto(v, commentRepository.countCommentsByVote(v))).collect(Collectors.toList());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(mypageVotes,pageVotes.getNumber(), pageVotes.getTotalElements(),pageVotes.getSize());
        return mypageVoteDto;
    }
    // 유저가 좋아요한 투표 조회
    public MypageVoteDto findups(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<Up> pageUps =  upRepository.findByuser(pageRequest,user);
        List<VoteResponseDto> mypageVotes = pageUps.stream().map(v -> VoteResponseDto.createVoteResponseDto(v.getVote(), commentRepository.countCommentsByVote(v.getVote()))).collect(Collectors.toList());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(mypageVotes,pageUps.getNumber(), pageUps.getTotalElements(),pageUps.getSize());
        return mypageVoteDto;
    }
    // 유저가 참여한 투표 조회
    public MypageVoteDto finduserVotes(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<UserVote> pageUps =  userVoteRepository.findByuser(pageRequest,user);
        List<VoteResponseDto> mypageVotes = pageUps.stream().map(v -> VoteResponseDto.createVoteResponseDto(v.getVote(), commentRepository.countCommentsByVote(v.getVote()))).collect(Collectors.toList());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(mypageVotes,pageUps.getNumber(), pageUps.getTotalElements(),pageUps.getSize());
        return mypageVoteDto;
    }
    // 유저가 북마크한 투표 조회
    public MypageVoteDto findbookMarks(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<Bookmark> pageUps =  bookmarkRepository.findByuser(pageRequest,user);
        List<VoteResponseDto> mypageVotes = pageUps.stream().map(v -> VoteResponseDto.createVoteResponseDto(v.getVote(), commentRepository.countCommentsByVote(v.getVote()))).collect(Collectors.toList());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(mypageVotes,pageUps.getNumber(), pageUps.getTotalElements(),pageUps.getSize());
        return mypageVoteDto;
    }
}
