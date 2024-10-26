import { Tabs,Flex,Radio,Space, Table, Tag,Button } from 'antd';
import React, { useState } from 'react';
import type { TableProps } from 'antd';
import VoteListAll from './VoteListAll'
import VoteListCategory from './VoteListCategory'
function VoteList() {
    return(
    <Tabs defaultActiveKey="1" centered>
        <Tabs.TabPane tab="전체" key="1">
             <VoteListAll />
        </Tabs.TabPane>
        <Tabs.TabPane tab="카테고리 별" key="2">
             <VoteListCategory />
        </Tabs.TabPane>
    </Tabs>
    );
}

export default VoteList;