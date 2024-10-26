
import {Table} from 'antd'
import React, { useState } from 'react';
function MypageVoteBookmarkList() {
    const [current,setcurrent] = useState(1); // 현재 페이지 넘버
    const dataSource = [
                         {
                              key: '1',
                              title: 'dwqqqqqqqqqqqqqdddddddddqqqqqq',
                              up: 32,
                              comment: 30,
                              view:20,
                         },
                         {
                                key: '1',
                                title: 'dwqqqqqqqqqqqqqdddddddddqqqqqq',
                                up: 32,
                                comment: 30,
                                view:20,
                         },
                         {
                                key: '1',
                                title: 'dwqqqqqqqqqqqqqdddddddddqqqqqq',
                                up: 32,
                                comment: 30,
                                view:20,
                         },
                         ];

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
                                dataIndex: 'comment',
                                key: 'comment',
                                align: 'center'
                         },
                         {
                                title: '조회',
                                dataIndex: 'view',
                                key: 'view',
                                align: 'center'
                         },
                       ];
        return (
                    <div>
                        <Table dataSource={dataSource} columns={columns}
                             pagination={{
                                       position: ['bottomCenter'], // 페이지네이션을 아래 가운데로 배치
                                       pageSize: 5, // 한 페이지에 5개 항목
                                       current: current, // 현재 페이지 설정
                                       onChange: (page, pageSize) => {
                                             setcurrent(page);
                                             console.log(page, pageSize);
                                       },
                            }}/>;
                        <br />
                    </div>
        );
    }

export default MypageVoteBookmarkList;


