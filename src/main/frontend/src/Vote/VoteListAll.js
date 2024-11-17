
import {Input,Typography,Button,Flex,Select,message} from 'antd'
import React, { useState,useEffect } from 'react';
import ReactCardSlider from '../Card/ReactCardSlider';
import { EditOutlined } from '@ant-design/icons';
import { Link,useLocation  } from 'react-router-dom';
import { useAuth } from '../Auth/AuthContext';
import axios  from 'axios';
const { Search } = Input;
const { Title } = Typography;

function VoteListAll(props) {
    const { isLoggedIn, logout,login  } = useAuth();
    const [slides,setslides] = useState([]);
    const [page,setpage] = useState(1);
    const [hasNext,sethasNext] =useState(false);
    const [SearchValue,setSearchValue] = useState(null);
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
    const styles =
    {
        cardBox:
        {
           width: '1200px',
           margin: '0 auto',
           marginTop: '100px',
        },
    }
    const [OrderValue, setOrderValue] = useState('startDay');
    // 정렬 설정 함수
    const handleChangeorder = (value) =>
    {
                  setOrderValue((value))
                  // 검색 키워드가 있다면
                  if(SearchValue != null)
                  {
                      axios.get('/api/votes/all?sort=' + value + '&title=' + SearchValue)
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
                       axios.get('/api/votes/all?sort=' + value)
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
    // 검색 함수
    function onSearch(value)
    {
              if(value.length == 0 || value.length > 20)
              {
                 message.error('검색 내용은 최대 20자 이하로 작성해주세요')
              }
              else
              {
                 setSearchValue(value);
                 axios.get('/api/votes/all?sort=' + OrderValue + '&title=' + value)
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
    }

     useEffect(() => {
                  const SearchValue = props.search;
                  if(SearchValue != null)
                  {
                      axios.get('/api/votes/all?sort=' + OrderValue + '&title=' + SearchValue)
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
                      axios.get('/api/votes/all?sort=' + OrderValue)
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
     }, []);
     
    return(
         <div>
              <div style={{width:'700px',margin:'0 auto'}}>
                             <Search
                                    placeholder="검색을 통해 다양한 투표를 참여해보세요!"
                                    allowClear
                                    size="large"
                                    enterButton
                                    onSearch={onSearch}
                            />
              </div>
              <div style={styles.cardBox}>
                            <Flex justify="space-between" align="center">
                                  <Title color="primary" level={3}>투표 참여</Title>
                                  {/* 정렬 선택 버튼 */}
                                  <Select
                                       showSearch
                                       placeholder="정렬 선택"
                                       onChange={handleChangeorder}
                                       filterOption={(input, option) =>
                                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                       }
                                       size="large"
                                       value={OrderValue}
                                       options={orders}
                                  />
                            </Flex>
                            {/* 투표 리스트 순차 로딩 */}
                            <ReactCardSlider slides={slides} hasNext={hasNext} page={page} OrderValue={OrderValue}/>
              </div>
              {/* 로그인한 상태이면 */}
              { isLoggedIn ? (
                            <Flex justify="center" style={{margin:200}}>
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

export default VoteListAll;