import React, { useState,useEffect } from 'react';
import {Table} from 'antd'
import axios from 'axios'
function MypageVoteLikeList() {
    const [page,setpage] = useState(1);
    const [pageSize,setpageSize] = useState(10)
    const [total,settotal] = useState(0)
    const [dataSource,setdataSource] = useState([])
const [focusedRow, setFocusedRow] = useState(null); // 포커스된 행을 추적
useEffect( () => {

   axios.get('/api/ups/mypage')
          .then((res) => {
                              const newSlides = res.data.vote.map((v) => ({
                                                  key: v.id,
                                                  title: v.title,
                                                  up: v.up,
                                                  commentCount: v.commentCount,
                                                  link : '/votedetail/' + v.id,
                                                 }));
                              setdataSource(newSlides); // slides를 새로운 배열로 설정
          })
},[])
    const columns = [
                         {
                                title: '제목',
                                dataIndex: 'title',
                                key: 'title',

                         },
                         {
                                title: '좋아요',
                                dataIndex: 'up',
                                key: 'up',
                                align: 'center'
                         },
                         {
                                title: '댓글',
                                dataIndex: 'commentCount',
                                key: 'commentCount',
                                align: 'center'
                         },
  ];


        return (
                    <div>
                        <Table dataSource={dataSource} columns={columns}
                             pagination={{
                                       total : total,
                                       position: ['bottomCenter'], // 페이지네이션을 아래 가운데로 배치
                                       pageSize: pageSize, // 한 페이지에 5개 항목
                                       current: page, // 현재 페이지 설정
                                       onChange: (page, pageSize) => {
                                            setpage(page + 1);
                                            axios.get('/api/ups/mypage?page=' + page + 1)
                                               .then((res) => {
                                                const newSlides = res.data.vote.map((v) => ({
                                                                         key: v.id,
                                                                         title: v.title,
                                                                         up: v.up,
                                                                         commentCount: v.commentCount,
                                                                    }));
                                                setdataSource(newSlides); // slides를 새로운 배열로 설정
                                               })
                                       },
                            }}
                            onRow={(record) => ({
                                    onClick: () => {
                                      // 클릭 시 해당 링크로 이동
                                      window.location.href = record.link;
                                    },
                                    onMouseEnter: () => {
                                      // 마우스 올리면 해당 행에 포커스 적용
                                      setFocusedRow(record.key);
                                    },
                                    onMouseLeave: () => {
                                      // 마우스를 떼면 포커스 제거
                                      setFocusedRow(null);
                                    },
                                  })}
                                  rowClassName={(record) => {
                                    // 포커스된 행에 특정 클래스 추가
                                    return record.key === focusedRow ? 'focused-row' : '';
                                  }}
                            />;
                        <br />
                    </div>
        );
    }

export default MypageVoteLikeList;


