package com.react.voteproject.service;


import com.react.voteproject.dto.MypageVoteDto;
import com.react.voteproject.dto.VoteWithCommentCountDTO;
import com.react.voteproject.entity.*;
import com.react.voteproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final VoteRepository voteRepository;
    private final UpRepository upRepository;
    private final UserVoteRepository userVoteRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    // 유저가 작성한 투표 조회
    @Transactional(readOnly = true)
    public MypageVoteDto findVotes(Pageable pageable, User user ){
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<VoteWithCommentCountDTO> votesWithCommentCount = voteRepository.findVotesWithCommentCount(pageRequest, user.getUserId());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(votesWithCommentCount.getContent(),votesWithCommentCount.getNumber(), votesWithCommentCount.getTotalElements(),votesWithCommentCount.getSize());
        return mypageVoteDto;
    }

    // 유저가 좋아요한 투표 조회
    @Transactional(readOnly = true)
    public MypageVoteDto findups(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<VoteWithCommentCountDTO> votesWithCommentCount = voteRepository.findUpVotesWithCommentCount(pageRequest, user.getUserId());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(votesWithCommentCount.getContent(),votesWithCommentCount.getNumber(), votesWithCommentCount.getTotalElements(),votesWithCommentCount.getSize());
        return mypageVoteDto;
    }

    // 유저가 참여한 투표 조회
    @Transactional(readOnly = true)
    public MypageVoteDto finduserVotes(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<VoteWithCommentCountDTO> votesWithCommentCount = voteRepository.findUserVotesWithCommentCount(pageRequest, user.getUserId());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(votesWithCommentCount.getContent(),votesWithCommentCount.getNumber(), votesWithCommentCount.getTotalElements(),votesWithCommentCount.getSize());
        return mypageVoteDto;
    }
    // 유저가 북마크한 투표 조회
    @Transactional(readOnly = true)
    public MypageVoteDto findbookMarks(Pageable pageable, User user) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page > 0 ? page - 1 : page, 10);
        Page<VoteWithCommentCountDTO> votesWithCommentCount = voteRepository.findBookmarkVotesWithCommentCount(pageRequest, user.getUserId());
        MypageVoteDto mypageVoteDto = MypageVoteDto.createMypageVoteDto(votesWithCommentCount.getContent(),votesWithCommentCount.getNumber(), votesWithCommentCount.getTotalElements(),votesWithCommentCount.getSize());
        return mypageVoteDto;
    }
}
