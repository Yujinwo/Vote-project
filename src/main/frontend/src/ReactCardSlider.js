"use strict";

import React, { useEffect, useState } from "react";
import { MdChevronLeft, MdChevronRight } from "react-icons/md"; // 아이콘 라이브러리에서 아이콘을 import
import { Link } from "react-router-dom"; // Link import
import { Progress, Flex,Button,Spin  } from "antd"; // Ant Design의 Progress 컴포넌트 import
import "./Slider.css"; // CSS 파일 import
import { BulbOutlined, CalendarOutlined, UserOutlined, LikeOutlined, CommentOutlined } from '@ant-design/icons';

const ReactCardSlider = (props) => {
  const [visibleSlides, setVisibleSlides] = useState(props.slides.slice(0, 5)); // 처음에 보일 슬라이드 수
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 관리

  const slideLeft = () => {
    const slider = document.getElementById("slider");
    slider.scrollLeft -= 500; // 슬라이드 왼쪽으로 이동
  };

  const slideRight = () => {
    const slider = document.getElementById("slider");
    slider.scrollLeft += 500; // 슬라이드 오른쪽으로 이동
    // 슬라이드 끝에 도달했는지 확인
    if (slider.scrollLeft + slider.clientWidth >= slider.scrollWidth) {
      loadMoreSlides(); // 슬라이드 추가 로드
    }
  };

 // 슬라이드의 끝에 도달했을 때 추가 슬라이드를 로드하는 함수
   const loadMoreSlides = () => {
     if (isLoading || visibleSlides.length >= props.slides.length) return; // 이미 로딩 중이거나 모든 슬라이드가 로드된 경우 종료

     setIsLoading(true);

     // 1초 후에 추가 슬라이드를 로드
     setTimeout(() => {
       const newSlides = props.slides.slice(visibleSlides.length, visibleSlides.length + 5); // 다음 5개 슬라이드 로드
       setVisibleSlides((prevSlides) => [...prevSlides, ...newSlides]); // 기존 슬라이드에 추가
       setIsLoading(false);
     }, 500);
   };

  // 슬라이드의 scroll 이벤트 리스너 등록
  useEffect(() => {
    const slider = document.getElementById("slider");

    const handleScroll = () => {
      if (slider.scrollLeft + slider.clientWidth >= slider.scrollWidth) { // 슬라이드 끝에 도달했을 때
        loadMoreSlides(); // 슬라이드 추가 로드
      }
    };

    slider.addEventListener("scroll", handleScroll); // scroll 이벤트 리스너 추가

    return () => {
      slider.removeEventListener("scroll", handleScroll); // 컴포넌트 언마운트 시 이벤트 리스너 제거
    };
  }, [visibleSlides, isLoading]); // 의존성 배열에 visibleSlides와 isLoading 추가

  return (
    <div id="main-slider-container">
      <MdChevronLeft size={40} className="slider-icon left" onClick={slideLeft} />
      <div id="slider" style={{ overflowX: 'scroll', whiteSpace: 'nowrap' }}>
        {visibleSlides.map((slide, index) => (
          <div className="slider-card" key={index}>
            <Link to={`/votedetail/${slide.id}`}>
              <div
                className="slider-card-image"
                style={{ backgroundSize: "cover" }}
              >
                <span className="slider-card-cetagory">{slide.category}</span>
              </div>
              <BulbOutlined />
              <p className="slider-card-title">{slide.title}</p>
              <CalendarOutlined />
              <div className="slider-card-day">
                <p className="slider-card-staryday">{slide.staryday}</p>
                <p className="slider-card-to">~</p>
                <p className="slider-card-endday">{slide.endday}</p>
              </div>
              <UserOutlined />
              <p className="slider-card-writer">{slide.writer}</p>
              <p className="slider-card-rate">
                <Progress percent={100} size="small" format={() => `${slide.rate}명`} />
              </p>
            </Link>
            <Flex justify="center" align="center" style={{ borderTop: '1px solid #1677ff', marginTop: '30px', marginBottom: '10px' }}>
              <div style={{ marginTop: 10 }}>
                <Button>
                  <LikeOutlined />
                  <span> {slide.up} </span>
                </Button>
                <Button>
                  <CommentOutlined />
                  <span> {slide.commentcount} </span>
                </Button>
              </div>
            </Flex>
          </div>
        ))}
        {isLoading && ( // 로딩 중일 때 Spin 컴포넌트 표시
                  <Flex justify="end" align="center">
                      <div style={{ position:'absolute',top:'135px' }}>
                        <Spin size="large" /> {/* 로딩 스피너 */}
                      </div>
                   </Flex>
                )}
      </div>
      <MdChevronRight size={40} className="slider-icon right" onClick={slideRight} />
    </div>
  );
};

export default ReactCardSlider; // 컴포넌트를 export
