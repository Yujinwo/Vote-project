"use strict";

import React, { useEffect, useState } from "react";
import { MdChevronLeft, MdChevronRight } from "react-icons/md";
import { Link } from "react-router-dom";
import { Progress, Flex,Button,Spin ,Empty } from "antd";
import "../css/Slider.css";
import { BulbOutlined, CalendarOutlined, UserOutlined, LikeOutlined, CommentOutlined } from '@ant-design/icons';
import axios from 'axios'
const ReactCardSlider = (props) => {
  const [visibleSlides, setVisibleSlides] = useState(props.slides.slice(0, props.slides.length)); // 처음에 보일 슬라이드 수
  const [isLoading, setIsLoading] = useState(false);
  const [page,setpage] = useState(props.page);
  const [hasNext,sethasNext] =useState(props.hasNext);
  const [order,setorder] =useState(props.OrderValue);
  const [category,setcategory] =useState(props.category);

  // 슬라이더 왼쪽 버튼
  const slideLeft = () =>
  {
    const slider = document.getElementById("slider");
    slider.scrollLeft -= 500; // 슬라이드 왼쪽으로 이동
  };

  // 슬라이더 오른쪽 버튼
  const slideRight = () =>
  {
    const slider = document.getElementById("slider");
    slider.scrollLeft += 500; // 슬라이드 오른쪽으로 이동

    // 슬라이드 끝에 도달했는지 확인
    if (slider.scrollLeft + slider.clientWidth >= slider.scrollWidth)
    {
      loadMoreSlides(); // 슬라이드 추가 로드
    }
  };

   // 슬라이드의 끝에 도달했을 때 추가 슬라이드를 로드하는 함수
   const loadMoreSlides = () =>
   {
     if (!hasNext) return; // 이미 로딩 중이거나 모든 슬라이드가 로드된 경우 종료
     setIsLoading(true);
     // 0.2초 후에 추가 슬라이드를 로드
     setTimeout(() =>
     {
       // 다음 데이터 존재 && 카테고리 미존재
       if(hasNext && !category)
       {
                axios.get('/api/votes/all?page=' + (page + 1) + '&sort=' + order )
                  .then((res) =>
                  {
                       const newSlides = res.data.vote.map((v) =>
                       ({
                              id: v.id,
                              category: v.category,
                              title: v.title,
                              startDay: v.startDay,
                              endDay: v.endDay,
                              writer: v.user.user_nick,
                              rate: v.optionCountTotal,
                              up: v.up,
                              commentCount: v.commentCount,
                       }));
                       setpage(res.data.page + 1)
                       // 다음 데이터 존재 여부
                       sethasNext(res.data.hasNext);
                       // 기존 데이터에 새로운 데이터 추가
                       setVisibleSlides((prevSlides) => [...prevSlides, ...newSlides]);
                       setIsLoading(false);
                  })
       }
       // 다음 데이터 존재 && 카테고리 존재
       else if(hasNext && category)
       {
                axios.get('/api/votes/all?page=' + (page + 1) + '&sort=' + order + '&category=' + category)
                  .then((res) =>
                  {
                       const newSlides = res.data.vote.map((v) =>
                       ({
                               id: v.id,
                               category: v.category,
                               title: v.title,
                               startDay: v.startDay,
                               endDay: v.endDay,
                               writer: v.user.user_nick,
                               rate: v.optionCountTotal,
                               up: v.up,
                               commentCount: v.commentCount,
                       }));
                       setpage(res.data.page + 1)
                       // 다음 데이터 존재 여부
                       sethasNext(res.data.hasNext);
                       // 기존 데이터에 새로운 데이터 추가
                       setVisibleSlides((prevSlides) => [...prevSlides, ...newSlides]); // 기존 슬라이드에 추가
                       setIsLoading(false);
                })
       }
     }, 200);
   };

// 처음 데이터 설정
useEffect(() =>
{
       setVisibleSlides(props.slides.slice(0, props.slides.length));
}, []); // 의존성 배열에 visibleSlides와 isLoading 추가

// 로딩 상태 변경
useEffect(() =>
{
   if(visibleSlides.length != 0)
   {
        const slider = document.getElementById("slider");
           const handleScroll = () =>
           {
             if (slider.scrollLeft + slider.clientWidth >= slider.scrollWidth)
             { // 슬라이드 끝에 도달했을 때
               loadMoreSlides(); // 슬라이드 추가 로드
             }
           };
           return () =>
           {
               slider.removeEventListener("scroll", handleScroll); // 컴포넌트 언마운트 시 이벤트 리스너 제거
           };
   }
}, [isLoading]);

// props 데이터 상태 변경
useEffect(() =>
{
    setVisibleSlides(props.slides.slice(0, props.slides.length));
    sethasNext(props.hasNext);
    setpage(props.page);
    setorder(props.OrderValue)
    setcategory(props.category);
}, [props]);

 return (
    visibleSlides.length != 0 ? (
        <div id="main-slider-container">
          <MdChevronLeft size={40} className="slider-icon left" onClick={slideLeft} />
          <div id="slider" style={{ overflowX: 'scroll', whiteSpace: 'nowrap' }}>
            {/* 데이터 순차 로딩 */}
            {visibleSlides.map((slide, index) => (
              <div className="slider-card" key={index}>
                <Link to={`/votedetail/${slide.id}`}>
                  {/* 카테고리 */}
                  <div
                    className="slider-card-image"
                    style={{ backgroundSize: "cover" }}
                  >
                    <span className="slider-card-cetagory">{slide.category}</span>
                  </div>
                  <BulbOutlined />
                  {/* 제목 */}
                  <p className="slider-card-title">{slide.title}</p>
                  <CalendarOutlined />
                  {/* 기간 */}
                  <div className="slider-card-day">
                    <p className="slider-card-staryday">{slide.startDay}</p>
                    <p className="slider-card-to">~</p>
                    <p className="slider-card-endday">{slide.endDay}</p>
                  </div>
                  <UserOutlined />
                  {/* 작성자 */}
                  <p className="slider-card-writer">{slide.writer}</p>
                  {/* 참여자 수 */}
                  <p className="slider-card-rate">
                    <Progress percent={100} size="small" format={() => `${slide.rate}명`} />
                  </p>
                </Link>
                <Flex justify="center" align="center" style={{ borderTop: '1px solid #1677ff', marginTop: '30px', marginBottom: '10px' }}>
                  <div style={{ marginTop: 10 }}>
                    {/* 좋아요 수 */}
                    <Button>
                      <LikeOutlined />
                      <span> {slide.up} </span>
                    </Button>
                    {/* 댓글 수 */}
                    <Button>
                      <CommentOutlined />
                      <span> {slide.commentCount} </span>
                    </Button>
                  </div>
                </Flex>
              </div>
            ))}
            {/* 로딩 중일 때 Spin 컴포넌트 표시 */}
            {isLoading && (
                      <Flex justify="end" align="center">
                          <div style={{ position:'absolute',top:'135px' }}>
                            <Spin size="large" />
                          </div>
                       </Flex>
             )}
          </div>
          <MdChevronRight size={40} className="slider-icon right" onClick={slideRight} />
        </div>
    ) : ( <div> <Empty description="데이터가 없습니다." style={{marginBottom:100,marginTop:50}} /> </div> )
  )
};

export default ReactCardSlider;
