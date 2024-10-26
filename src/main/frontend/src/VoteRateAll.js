
import { Tabs,Flex,Radio,Space, Table, Tag,Button,Progress ,Input } from 'antd';
import React, { useState } from 'react';
import type { TableProps } from 'antd';
const { Search } = Input;

function VoteRateAll() {
          const columns = [
            {
              title: '순위',
              dataIndex: 'rank',
              key: 'rank',
              align: 'center', // 가운데 정렬
            },
            {
              title: '유저',
              dataIndex: 'user',
              key: 'user',
              align: 'center', // 가운데 정렬
            },
            {
              title: '참여율',
              dataIndex: 'rate',
              key: 'rate',
              align: 'center', // 가운데 정렬
              render: (text) => (
                      <>

                        <Progress type="dashboard" percent={text} format={() => `${text} %`}/> {/* Progress 컴포넌트 사용 */}
                      </>
              ),
            },
            {
              title: '투표 작성',
              dataIndex: 'createdvote',
              key: 'createdvote',
              align: 'center', // 가운데 정렬
            },
            {
              title: '좋아요',
              dataIndex: 'up',
              key: 'up',
              align: 'center', // 가운데 정렬
            },
            {
              title: '댓글',
              dataIndex: 'comment',
              key: 'comment',
              align: 'center', // 가운데 정렬
            },
            {
              title: '활동 카테고리',
              dataIndex: 'category',
              key: 'category',
              align: 'center', // 가운데 정렬
              render: (text) => <Tag color="blue">{text}</Tag>, // 태그 형태로 데이터 표시
            },
          ];


          // 특정 유저 데이터를 정의
          const firstUserData = {
              key: 100, // 유일한 키 값을 지정
              rank: 300, // 첫 번째 랭크
              user: "firstUser", // 유저 이름
              rate: "50", // 평점
              createdvote: 30,  // 작성한 투표 수
              up: 20, // 좋아요 수
              comment: 5, // 댓글 수
              category: "음식", // 카테고리
          };
       // 예시 데이터
        const allData = Array.from({ length: 100 }, (v, k) => ({
          key: k,
          rank: k,
          user:"dbwlsdn0125",
          rate:"30",
          createdvote: 10,
          up : 10,
          comment : 100,
          category: "음식",
        }));
        // 첫 번째 유저 데이터를 배열의 맨 앞에 추가
        allData.unshift(firstUserData);


        const [visibleData, setVisibleData] = useState(allData.slice(0, 10)); // 초기 10개 데이터
        const [dataCount, setDataCount] = useState(10); // 현재 표시된 데이터 개수
        // 선택된 값을 상태로 관리
        const [selectedValue, setSelectedValue] = useState('today'); // 초기값 'a'

        const handleLoadMore = () => {
           var nextDataCount = dataCount;
           if(allData.length < dataCount + 10)
           {
               nextDataCount = allData.length; // 10개씩 추가
           }
           else {
               nextDataCount = dataCount + 10; // 10개씩 추가
           }
           setVisibleData(allData.slice(0, nextDataCount));
           setDataCount(nextDataCount);
        };
        // onChange 핸들러
        const handleChange = (e) => {
            setSelectedValue(e.target.value); // 선택된 값을 상태에 저장
            console.log('선택한 값:', e.target.value); // 선택한 값 출력
        };
        function onSearch(value) {
              console.log(value)
        }
    return(
            <div>
                   <Flex gap="middle" justify="center">
                       <Radio.Group defaultValue="a" buttonStyle="solid" onChange={handleChange}>
                            <Radio.Button value="today">오늘</Radio.Button>
                            <Radio.Button value="thismonth">이번달</Radio.Button>
                            <Radio.Button value="thisyear">이번년도</Radio.Button>
                       </Radio.Group>
                   </Flex>
                   <Flex style={{marginTop: '20px', margin:'0 auto', width:'1200px'}} align="center" justify="space-between">
                         <p style={{fontWeight:'bold',fontSize:'20px'}}> 전체 유저 참여율 통계 </p>
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
                        <Table dataSource={visibleData} columns={columns} pagination={false}
                          rowClassName={(record) => record.user === "firstUser" ? 'highlighted' : ''} />
                              {dataCount < allData.length && (
                              <div className="button-container"> {/* 가운데 정렬을 위한 버튼 컨테이너 */}
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