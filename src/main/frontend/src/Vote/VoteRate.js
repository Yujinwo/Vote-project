
import { Tabs,Flex,Radio,Space, Table, Tag,Button } from 'antd';
import React, { useState } from 'react';
import type { TableProps } from 'antd';
import VoteRateAll from './VoteRateAll'
import VoteRateCategory from './VoteRateCategory'
function VoteRate() {
    return(
        <Tabs defaultActiveKey="1" centered>
            <Tabs.TabPane tab="전체" key="1">
                 <VoteRateAll />
            </Tabs.TabPane>
            <Tabs.TabPane tab="카테고리 별" key="2">
                 <VoteRateCategory />
            </Tabs.TabPane>
        </Tabs>
    );
}

export default VoteRate;