package com.react.voteproject.service;


import com.react.voteproject.category.category_enum;
import com.react.voteproject.context.AuthContext;
import com.react.voteproject.dto.*;
import com.react.voteproject.entity.*;
import com.react.voteproject.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
     private final VoteRepository voteRepository;
     private final VoteOptionRepository voteOptionRepository;
     private final UserVoteRepository userVoteRepository;
     private final UpRepository upRepository;
     private final BookmarkRepository bookmarkRepository;
     private final CommentRepository commentRepository;
     private final EntityManager em;


     // 투표 작성
     @Transactional
     public Boolean write(VoteWriteDto voteWriteDto) {
        Optional<Vote> vote = Optional.of(voteRepository.save(voteWriteDto.createVote()));
        if(vote.isEmpty())
        {
            return false;
        }
        em.flush();
        em.clear();
        // 투표 선택지 저장
        for (String option : voteWriteDto.getChoices())
        {
            Optional<VoteOption> voteOption = Optional.ofNullable(voteOptionRepository.save(VoteOption.builder().content(option).vote(vote.get()).build()));
            if(voteOption.isEmpty())
            {
                return false;
            }
        }
        return true;

    }
    // 투표 수정
    @Transactional
    public Boolean update(VoteUpdateDto voteUpdateDto) {

        Optional<Vote> vote = voteRepository.findById(voteUpdateDto.getVote_id());
        if(vote.isPresent()){
                // 댓글 작성자와 일치하지 않다면
                if(!vote.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                    return false;
                }
                List<LocalDateTime> dateTimeList = voteUpdateDto.changeDayFormat();
                vote.get().changeVoteContent(voteUpdateDto.getTitle(), voteUpdateDto.getCategory(), dateTimeList.get(0),dateTimeList.get(1));
                List<VoteOption> voteOption = voteOptionRepository.findByvote(vote.get());
                if(!voteOption.isEmpty()) {

                    for (int i = 0; i < voteOption.size(); i++) {
                        String option = voteOption.get(i).getContent();
                        if(!voteUpdateDto.getChoices().get(i).equals(option)) {
                            voteOption.get(i).changeContent(voteUpdateDto.getChoices().get(i));
                        }
                    }
                    return true;
                }
        }
        return false;
    }
    // 투표 상세 조회
    @Transactional
    public VoteDetailDataDto findvotesId(Long id) {
        Optional<Vote> vote = voteRepository.findById(id);
        if(vote.isPresent()) {
            // 이미 투표 선택했는지 조회
            Long userSelectedId = null;

            // 투표 댓글 Slice 조회
            PageRequest pageRequest = PageRequest.of(0,10);
            Page<Comment> comments = commentRepository.findPageByVote(vote.get(), pageRequest);
            List<CommentResponseDto> commentList = comments.getContent().stream().map(CommentResponseDto::createCommentResponseDto).collect(Collectors.toList());

            // 투표 데이터 조회
            VoteResponseDto voteResponseDto = vote.map(v -> VoteResponseDto.createVoteResponseDto(v,comments.getTotalElements())).get();

            Boolean hasUp = false;
            Boolean hasBookmark = false;
            if(upRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth()).isPresent()) {
                hasUp = true;
            }

            if(bookmarkRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth()).isPresent()) {
                hasBookmark = true;
            }

            if(AuthContext.checkAuth()) {
                Optional<UserVote> userVote = userVoteRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth());
                if(userVote.isPresent())
                {
                    userSelectedId = userVote.get().getVoteOption().getId();
                    return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(),hasUp,hasBookmark);
                }
                else {
                    return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(), hasUp, hasBookmark);
                }
            }
            else {
                return VoteDetailDataDto.createVoteDetailDataDto(voteResponseDto,userSelectedId,commentList,comments.hasNext(), hasUp, hasBookmark);
            }
        }
        else {
            return new VoteDetailDataDto();
        }

    }
    // 유저 투표 참여 선택지 변경
    @Transactional
    public Boolean changeVoteOption(Long id) {
        Optional<VoteOption> voteOption = voteOptionRepository.findById(id);
        if(voteOption.isPresent()){
            VoteOption option = voteOption.get();

            // 기존 선택지 삭제 -> 변경한 선택지로 수정
            Optional<UserVote> userVote = userVoteRepository.findByVoteAndUser(voteOption.get().getVote(),AuthContext.getAuth());
            if(userVote.isPresent())
            {
                userVoteRepository.delete(userVote.get());
                userVote.get().getVoteOption().downCount();
                option.upCount();
            }
            else {
                option.upCount();
            }

            Optional<UserVote> save = Optional.of(userVoteRepository.save(voteOption.get().createUserVote()));
            if(save.isEmpty())
            {
                return false;
            }
            return true;
        }
        return false;
    }

    // 투표 삭제
    @Transactional
    public Boolean delete(Long voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if(vote.isPresent())
        {
            // 댓글 작성자와 일치하지 않다면
            if(!vote.get().getUser().getUserId().equals(AuthContext.getAuth().getUserId())) {
                return false;
            }
            // 투표 삭제
            voteRepository.delete(vote.get());
            return true;
        }
        return false;
    }
    // 좋아요
    @Transactional
    public Boolean changeUp(Long voteId) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if(vote.isPresent())
        {
            Optional<Up> up = upRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth());
            if(up.isPresent()) {
                // 좋아요 -1
                upRepository.delete(up.get());
                vote.get().changeUp(-1);
            }
            else {
                // 좋아요 +1
                Up saveUp = Up.builder().user(AuthContext.getAuth()).vote(vote.get()).build();
                Optional<Up> savedUp = Optional.of(upRepository.save(saveUp));
                if(savedUp.isEmpty()) {
                    return false;
                }
                vote.get().changeUp(1);
            }
            return true;
        }
        return false;
    }
    // 북마크
    @Transactional
    public Boolean changeBookmark(Long voteId) {

        Optional<Vote> vote = voteRepository.findById(voteId);
        if(vote.isPresent())
        {
            Optional<Bookmark> bookmark = bookmarkRepository.findByVoteAndUser(vote.get(),AuthContext.getAuth());
            if(bookmark.isPresent()) {
                // 북마크 해제
                bookmarkRepository.delete(bookmark.get());
            }
            else {
                // 북마크 등록
                Bookmark saveBookmark = Bookmark.builder().user(AuthContext.getAuth()).vote(vote.get()).build();
                Optional<Bookmark> savedBookmark = Optional.of(bookmarkRepository.save(saveBookmark));
                if(savedBookmark.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    // 투표 참여 리스트 조회
    @Transactional(readOnly = true)
    @Async
    public CompletableFuture<VoteHomeDataDto> findAll(Pageable pageable, String sort, String category, String searchTitle) {
         String[] sortFields = {"startDay","up","voting"};

         boolean contains = Arrays.asList(sortFields).contains(sort);
         if(!contains) {
             sort = "startDay";
         }
         int page = pageable.getPageNumber() - 1;
         if(pageable.getPageNumber() == 0) {
            page = 0;
         }

         if(category.isBlank()) {
             if(sort.equals("voting")) {
                 // 첫 번째 페이지, 10개
                 PageRequest pageRequest = PageRequest.of(page, 10);
                 Slice<Object[]> voteWithTotalCount = voteRepository.findVoteWithTotalCount(pageRequest);
                 List<VoteResponseDto> list = voteWithTotalCount.getContent().stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], commentRepository.countCommentsByVote((Vote) v[0]))).collect(Collectors.toList());
                 return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,voteWithTotalCount.hasNext(),voteWithTotalCount.getNumber()));
             }
             PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc(sort)));
             Slice<Vote> sliceVote = voteRepository.findAllByVote(pageRequest,searchTitle);
             List<VoteResponseDto> list = sliceVote.stream().map(v -> VoteResponseDto.createVoteResponseDto(v, commentRepository.countCommentsByVote(v))).collect(Collectors.toList());
             return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,sliceVote.hasNext(),sliceVote.getNumber()));
         }
         else {
             Boolean checkCategory = category_enum.fromCode(category);
             if(!checkCategory) {
                 category = "ENTERTAINMENT";
             }
             if(sort.equals("voting")) {
                 // 첫 번째 페이지, 10개
                 PageRequest pageRequest = PageRequest.of(page, 10);
                 Slice<Object[]> voteWithTotalCount = voteRepository.findVotesWithTotalCountByCategory(pageRequest,category);
                 List<VoteResponseDto> list = voteWithTotalCount.getContent().stream().map(v -> VoteResponseDto.createVoteResponseDto((Vote) v[0], commentRepository.countCommentsByVote((Vote) v[0]))).collect(Collectors.toList());
                 return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,voteWithTotalCount.hasNext(),voteWithTotalCount.getNumber()));
             }
             PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Order.desc(sort)));
             Slice<Vote> sliceVote = voteRepository.findAllByVoteAndCategory(pageRequest,category,searchTitle);
             List<VoteResponseDto> list = sliceVote.stream().map(v -> VoteResponseDto.createVoteResponseDto(v, commentRepository.countCommentsByVote(v))).collect(Collectors.toList());
             return CompletableFuture.completedFuture(VoteHomeDataDto.createVoteHomeDataDto(list,sliceVote.hasNext(),sliceVote.getNumber()));
         }
    }
    // 총 투표 수 , 인기 카테고리 조회
    @Transactional(readOnly = true)
    public HotCategoryAndTotalDto getSummary() {
        Object[] summary = voteRepository.findHotCategoryWithVoteCount();
        HotCategoryAndTotalDto hotCategoryAndTotalDto = HotCategoryAndTotalDto.createHotCategoryAndTotalDto(summary);
        return  hotCategoryAndTotalDto;
    }
    // 인기 투표 조회
    @Transactional(readOnly = true)
    public HotVoteandRankDto getHot() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Object[]> summary = voteRepository.findHotVote(pageRequest);
        List<HotVoteResponseDto> hotVoteResponseDto = summary.stream().map(v -> HotVoteResponseDto.createHotVoteResponseDto(v,0L)).collect(Collectors.toList());

        HotVoteandRankDto hotVoteandRankDto = HotVoteandRankDto.createHotVoteandRankDto(hotVoteResponseDto);
        return hotVoteandRankDto;
    }
    // 투표 추천 리스트 조회
    @Transactional(readOnly = true)
    public HotVoteandRankDto getRecommend() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Long user_id = AuthContext.getAuth() != null ? AuthContext.getAuth().getId() :null;
        List<Object[]> summary = voteRepository.findHotVotesExcludingUser(user_id,pageRequest);
        List<HotVoteResponseDto> hotVoteResponseDto = summary.stream().map(v -> HotVoteResponseDto.createHotVoteResponseDto(v,commentRepository.countCommentsByVote((Vote) v[0]))).collect(Collectors.toList());
        HotVoteandRankDto hotVoteandRankDto = HotVoteandRankDto.createHotVoteandRankDto(hotVoteResponseDto);
        return hotVoteandRankDto;

    }
}
