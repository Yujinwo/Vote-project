import { Tabs,Flex,Radio,Space, Table, Tag,Button } from 'antd';
import React, { useState } from 'react';
import type { TableProps } from 'antd';
import VoteListAll from './VoteListAll'
import VoteListCategory from './VoteListCategory'
import { useLocation  } from 'react-router-dom';
function VoteList() {
    const location = useLocation();
    // 넘겨받은 검색 키워드가 있다면
    var SearchValue = location.state?.value;

    return(
        <Tabs defaultActiveKey="1" centered>
            <Tabs.TabPane tab="전체" key="1">
                 <VoteListAll search={SearchValue} />
            </Tabs.TabPane>
            <Tabs.TabPane tab="카테고리 별" key="2">
                 <VoteListCategory />
            </Tabs.TabPane>
        </Tabs>
    );
}

export default VoteList;