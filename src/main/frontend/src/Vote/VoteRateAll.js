
import { Tabs,Flex,Radio,Space, Table, Tag,Button,Progress ,Input } from 'antd';
import React, { useState ,useEffect} from 'react';
import type { TableProps } from 'antd';
import axios from 'axios'
import { useAuth } from '../Auth/AuthContext';
const { Search } = Input;

function VoteRateAll() {
          const [allData,setallData] = useState([])
          const { isLoggedIn,userid } = useAuth();
          const columns =
          [
            {
              title: '순위',
              dataIndex: 'rank',
              key: 'rank',
              align: 'center',
            },
            {
              title: '유저',
              dataIndex: 'user',
              key: 'user',
              align: 'center',
            },
            {
              title: '참여율',
              dataIndex: 'rate',
              key: 'rate',
              align: 'center',
              render: (text) => (
                      <>

                        <Progress type="dashboard" percent={text} format={() => `${text} %`}/>
                      </>
              ),
            },
            {
              title: '투표 작성',
              dataIndex: 'vote',
              key: 'vote',
              align: 'center',
            },
            {
              title: '좋아요',
              dataIndex: 'up',
              key: 'up',
              align: 'center',
            },
            {
              title: '댓글',
              dataIndex: 'comment',
              key: 'comment',
              align: 'center',
            },
            {
              title: '활동 카테고리',
              dataIndex: 'category',
              key: 'category',
              align: 'center',
              render: (text) => <Tag color="blue">{text}</Tag>,
            },
          ];

          useEffect(() =>
          {
             if(isLoggedIn)
             {
                 // 내 유저 정보 통계 조회
                 axios.get('/api/uservotes/stats/search?id=' + userid )
                       .then((user) => {
                        const firstUserData =
                        {
                             key: 0,
                             rank: user.data[0].rank,
                             user:user.data[0].user_id,
                             rate:user.data[0].rate,
                             vote:user.data[0].vote ,
                             up : user.data[0].up,
                             comment : user.data[0].comment,
                             category: user.data[0].category
                        }
                        // 전체 유저 정보 통계 조회
                        axios.get('/api/uservotes/stats')
                          .then((res) =>
                          {
                              const newSlides = res.data.map((v) =>
                              ({
                                     key: v.rank,
                                     rank: v.rank,
                                     user:v.user_id,
                                     rate:v.rate,
                                     vote:v.vote ,
                                     up : v.up,
                                     comment : v.comment,
                                     category: v.category,
                              }));
                              newSlides.unshift(firstUserData);
                              setallData(newSlides);
                              setVisibleData(newSlides.slice(0,10))
                          })
                       })
            }
            else
            {
                        // 전체 유저 정보 통계 조회
                        axios.get('/api/uservotes/stats')
                          .then((res) =>
                          {
                               const newSlides = res.data.map((v) =>
                               ({
                                     key: v.rank,
                                     rank: v.rank,
                                     user:v.user_id,
                                     rate:v.rate,
                                     vote:v.vote ,
                                     up : v.up,
                                     comment : v.comment,
                                     category: v.category,
                               }));
                               setallData(newSlides);
                               setVisibleData(newSlides.slice(0,10))
                          })
            }
        },[])


        const [visibleData, setVisibleData] = useState(allData.slice(0, 10));
        const [dataCount, setDataCount] = useState(10);
        const [selectedValue, setSelectedValue] = useState('Thisyear');

        // 유저 정보 더보기 함수
        const handleLoadMore = () =>
        {
           var nextDataCount = dataCount;
           if(allData.length < dataCount + 10)
           {
               nextDataCount = allData.length;
           }
           else
           {
               nextDataCount = dataCount + 10;
           }
           setVisibleData(allData.slice(0, nextDataCount));
           setDataCount(nextDataCount);
        };
        // 기간 설정 함수
        const handleChange = (e) =>
        {
             var dayValue = e.target.value;
             setSelectedValue(dayValue);

             if(isLoggedIn)
             {
                   axios.get('/api/uservotes/stats/search?id=' + userid + '&day=' + dayValue)
                              .then((user) => {
                               const firstUserData =
                               {
                                    key: 0,
                                    rank: user.data[0].rank,
                                    user:user.data[0].user_id,
                                    rate:user.data[0].rate,
                                    vote:user.data[0].vote ,
                                    up : user.data[0].up,
                                    comment : user.data[0].comment,
                                    category: user.data[0].category
                               }
                               axios.get('/api/uservotes/stats?day=' + dayValue)
                                   .then((res) =>
                                   {
                                        const newSlides = res.data.map((v) =>
                                        ({
                                             key: v.rank,
                                             rank: v.rank,
                                             user:v.user_id,
                                             rate:v.rate,
                                             vote:v.vote ,
                                             up : v.up,
                                             comment : v.comment,
                                             category: v.category,
                                        }));
                                        newSlides.unshift(firstUserData);
                                        setallData(newSlides);
                                        setVisibleData(newSlides.slice(0,10))
                                   })

                              })
             }
             else
             {
                   axios.get('/api/uservotes/stats?day=' + dayValue)
                      .then((res) =>
                      {
                            const newSlides = res.data.map((v) =>
                            ({
                                  key: v.rank,
                                  rank: v.rank,
                                  user:v.user_id,
                                  rate:v.rate,
                                  vote:v.vote ,
                                  up : v.up,
                                  comment : v.comment,
                                  category: v.category,
                            }));
                            setallData(newSlides);
                            setVisibleData(newSlides.slice(0,10))
                      })

             }
        };
        function onSearch(value) {
                axios.get('/api/uservotes/stats/search?day=' + selectedValue + '&id=' + value)
                   .then((res) =>
                   {
                         const newSlides = res.data.map((v) =>
                         ({
                             key: v.rank,
                             rank: v.rank,
                             user:v.user_id,
                             rate:v.rate,
                             vote:v.vote ,
                             up : v.up,
                             comment : v.comment,
                             category: v.category,
                         }));
                         setallData(newSlides);
                         setVisibleData(newSlides.slice(0,1))
                   })
        }
    return(
            <div>
                   {/* 기간 버튼 */}
                   <Flex gap="middle" justify="center">
                       <Radio.Group defaultValue="Thisyear" buttonStyle="solid" onChange={handleChange}>
                            <Radio.Button value="Today">오늘</Radio.Button>
                            <Radio.Button value="Thismonth">이번달</Radio.Button>
                            <Radio.Button value="Thisyear">이번년도</Radio.Button>
                       </Radio.Group>
                   </Flex>
                   <Flex style={{marginTop: '20px', margin:'0 auto', width:'1200px'}} align="center" justify="space-between">
                         <p style={{fontWeight:'bold',fontSize:'20px'}}> 전체 유저 참여율 통계 </p>
                         {/* 유저 검색 버튼 */}
                         <Search
                                placeholder="유저 검색"
                                allowClear
                                size="middle"
                                enterButton
                                style={{width:200}}
                                onSearch={onSearch}
                         />
                   </Flex>

                   <div className="table-container">
                        {/* 테이블 */}
                        <Table dataSource={visibleData} columns={columns} pagination={false}
                          rowClassName={(record) => record.key === 0 ? 'highlighted' : ''} />
                              {dataCount < allData.length &&
                              (
                                  <div className="button-container">
                                            <Button onClick={handleLoadMore}>
                                              더보기
                                            </Button>
                                  </div>
                              )}
                   </div>
             </div>
    );
}

export default VoteRateAll;