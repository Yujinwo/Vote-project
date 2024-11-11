
import {Input,Typography,Button,Flex,Select,message} from 'antd'
import React, { useState,useEffect } from 'react';
import ReactCardSlider from './ReactCardSlider';
import { EditOutlined } from '@ant-design/icons';
import { Link,useLocation  } from 'react-router-dom';
import { useAuth } from './AuthContext';
import axios  from 'axios';
const { Search } = Input;
const { Title } = Typography;

function VoteListAll(props) {
    const { isLoggedIn, logout,login  } = useAuth();
    const [slides,setslides] = useState([]);
    const [page,setpage] = useState(1);
    const [hasNext,sethasNext] =useState(false);
    const [SearchValue,setSearchValue] = useState(null);
    const orders = [
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
    const styles = {
        cardBox: {
                  width: '1200px',
                  margin: '0 auto', // 좌우 중앙 정렬
                  marginTop: '100px',
        },

    }
    const [OrderValue, setOrderValue] = useState('startDay'); // 초기값 'a'
    const handleChangeorder = (value) => {
                  setOrderValue((value))
                  if(SearchValue != null) {
                      axios.get('/api/votes/all?sort=' + value + '&title=' + SearchValue)
                                               .then((res) => {
                                                           const newSlides = res.data.vote.map((v) => ({
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
                  else {
                       axios.get('/api/votes/all?sort=' + value)
                                                .then((res) => {
                                                               const newSlides = res.data.vote.map((v) => ({
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

     };

    function onSearch(value) {
              if(value.length == 0 || value.length > 20) {
                 message.error('검색 내용은 최대 20자 이하로 작성해주세요')
              }
              else {

                 setSearchValue(value);
                             axios.get('/api/votes/all?sort=' + OrderValue + '&title=' + value)
                                                                        .then((res) => {
                                                                                    const newSlides = res.data.vote.map((v) => ({
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

     useEffect(() => {
                  const SearchValue = props.search; // 'Hello from MyComponent!'
                  if(SearchValue != null) {
                      axios.get('/api/votes/all?sort=' + OrderValue + '&title=' + SearchValue)
                             .then((res) => {
                                         const newSlides = res.data.vote.map((v) => ({
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
                  else {
                      axios.get('/api/votes/all?sort=' + OrderValue)
                             .then((res) => {
                                         const newSlides = res.data.vote.map((v) => ({
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
                               <Select
                                                                    showSearch
                                                                    placeholder="정렬 선택"
                                                                    onChange={handleChangeorder} // 선택된 값을 가져오는 핸들러
                                                                    filterOption={(input, option) =>
                                                                          (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                                                    }
                                                                    size="large"
                                                                    value={OrderValue}
                                                                    options={orders}
                                                            />

                             </Flex>
                             <ReactCardSlider slides={slides} hasNext={hasNext} page={page} OrderValue={OrderValue}/>
              </div>
              { isLoggedIn ? (
                   <Flex justify="center">
                        <div style={styles.votewritebutton}>
                           <Link to="/votewrite">
                             <Button type="primary" shape="round" icon={<EditOutlined />} size={20}>
                                          투표 작성
                             </Button>
                           </Link>
                        </div>
                   </Flex>
              ) : ''}

         </div>

    );
}

export default VoteListAll;