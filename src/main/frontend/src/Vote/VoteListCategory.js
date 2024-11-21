
import {Input,Typography,Flex,Select,Button,message} from 'antd'
import ReactCardSlider from '../Card/ReactCardSlider';
import React, { useState,useEffect } from 'react';
import { EditOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { useAuth } from '../Auth/AuthContext';
import axios from  "../apiClient"
const { Search } = Input;
const { Title } = Typography;
function VoteListCategory() {
     const { isLoggedIn, logout,login  } = useAuth();
     const [slides,setslides] = useState([]);
     const [page,setpage] = useState(1);
     const [hasNext,sethasNext] =useState(false);
     const [CategoryValue, setCategoryValue] = useState("LIFESTYLE");
     const [OrderValue, setOrderValue] = useState('startDay');
     const [SearchValue,setSearchValue] = useState(null);
     const categorys =
     [
          {
              value: 'ENTERTAINMENT',
              label: '엔터테인먼트',
          },
          {
              value: 'SPORTS',
              label: '스포츠',
          },
          {
             value: 'FASHION_BEAUTY',
             label: '패션 및 부티',
          },
          {
             value: 'FOOD_CULINARY',
             label: '음식 및 요리',
          },
          {
             value: 'LIFESTYLE',
             label: '라이프스타일',
          },
          {
             value: 'GAMING_IT',
             label: '게임 및 IT',
          },
          {
             value: 'EDUCATION_LEARNING',
             label: '교육 및 학습',
          },
     ]
     const orders =
     [
          {
             value: 'startDay',
             label: '최신',
          },
          {
             value: 'voting',
             label: '참여율',
          },
          {
             value: 'up',
             label: '좋아요',
          },
     ]
     // 카테고리 선택 함수
     const handleChangecategory = (value) =>
     {
              setCategoryValue(value);
              if(SearchValue != null)
              {
                    axios.get('/api/votes/all?sort=' + OrderValue + "&category=" + value  + '&title=' + SearchValue)
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
                           setslides(newSlides);
                           setpage(res.data.page + 1)
                           sethasNext(res.data.hasNext);
                      })
              }
              else
              {
                  axios.get('/api/votes/all?sort=' + OrderValue + "&category=" + value)
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
                             setslides(newSlides);
                             setpage(res.data.page + 1)
                             sethasNext(res.data.hasNext);
                     })
              }
     };
     // 정렬 선택 함수
     const handleChangeorder = (value) =>
     {
              setOrderValue(value)
              if(SearchValue != null)
              {
                  axios.get('/api/votes/all?sort=' + value + "&category=" + CategoryValue + '&title=' + SearchValue)
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
                          setslides(newSlides);
                          setpage(res.data.page + 1)
                          sethasNext(res.data.hasNext);
                     })
              }
              else
              {
                  axios.get('/api/votes/all?sort=' + value + "&category=" + CategoryValue)
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
                           setslides(newSlides);
                           setpage(res.data.page + 1)
                           sethasNext(res.data.hasNext);
                     })
              }
     };

     const styles =
     {
             cardBox:
             {
                       width: '1200px',
                       margin: '0 auto', // 좌우 중앙 정렬
                       marginTop: '100px',
             },
     }

     useEffect(() =>
     {
                 axios.get('/api/votes/all?sort=' + OrderValue + "&category=" + CategoryValue)
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
                           setslides(newSlides); // slides를 새로운 배열로 설정
                           setpage(res.data.page + 1)
                           sethasNext(res.data.hasNext);
                   })
     }, []);

     function onSearch(value)
     {
               if(value.length == 0 || value.length > 20)
               {
                      message.error('검색 내용은 최대 20자 이하로 작성해주세요')
               }
               else
               {
                      setSearchValue(value)
                      axios.get('/api/votes/all?sort=' + OrderValue + "&category=" + CategoryValue + '&title=' + value)
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
                              setslides(newSlides); // slides를 새로운 배열로 설정
                              setpage(res.data.page + 1)
                              sethasNext(res.data.hasNext);
                        })
             }
      }
    return(
         <div>
             <div style={{width:'700px',margin:'0 auto'}}>
                 <Search
                        placeholder="검색을 통해 다양한 투표를 참여해보세요!"
                        allowClear
                        enterButton
                        size="large"
                        onSearch={onSearch}
                 />
             </div>
             <div style={styles.cardBox}>
                        <Flex style={{marginBottom:'10px'}}justify="space-between" align="center">
                              {/* 카테고리 선택 버튼 */}
                              <Select
                                      showSearch
                                      placeholder="카테고리 선택"
                                      onChange={handleChangecategory} // 선택된 값을 가져오는 핸들러
                                      filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                      }
                                      value={CategoryValue}
                                      options={categorys}
                                      size="large"
                              />
                              {/* 정렬 선택 버튼 */}
                              <Select
                                      showSearch
                                      placeholder="정렬 선택"
                                      onChange={handleChangeorder} // 선택된 값을 가져오는 핸들러
                                      filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                      }
                                      value={OrderValue}
                                      options={orders}
                                      size="large"
                              />
                        </Flex>
                        {/* 투표 리스트 순차 로딩 */}
                        <ReactCardSlider slides={slides} hasNext={hasNext} page={page} OrderValue={OrderValue} category={CategoryValue}/>
             </div>
              {/* 로그인한 상태이면 */}
              { isLoggedIn ? (
                        <Flex justify="center">
                            <div style={styles.votewritebutton}>
                               {/* 투표 작성 버튼 */}
                               <Link to="/votewrite">
                                 <Button type="primary" shape="round" icon={<EditOutlined />} size={20}>
                                              투표 작성
                                 </Button>
                               </Link>
                            </div>
                       </Flex>
                 ) : ''
              }
         </div>

    );
}

export default VoteListCategory;