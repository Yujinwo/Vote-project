import React, { useState,useEffect } from 'react';
import { Flex, Progress,Tabs,Table,Pagination,Tag } from 'antd';
import { ContainerOutlined,LikeOutlined,CommentOutlined } from '@ant-design/icons';
import MypageVoteWriteList from './MypageVoteWriteList'
import MypageVoteLikeList from './MypageVoteLikeList'
import MypageVoteSelectList from './MypageVoteSelectList'
import MypageVoteBookmarkList from './MypageVoteBookmarkList'
import axios from 'axios'
function Mypage() {

   const [userdata,setuserdata] = useState({});

   useEffect(() => {
        axios.get('/api/users/stats')
              .then((res) => {
                   setuserdata({usernick:res.data.user_nick,userid:res.data.user_id,rate:res.data.rate,vote:res.data.vote,up:res.data.up,comment:res.data.comment})
              })
   },[])

   return (
    <div>
        <div style={{width:'600px',margin:'0 auto',marginBottom:'20px'}}>
            <div style={{border:'1px solid',borderRadius:10,padding:20}}>
               <Flex gap="small" vertical>
                   <div style={{marginLeft:13,fontSize:20,fontWeight:'bold'}}>
                       {userdata.usernick}({userdata.userid})
                   </div>
               </Flex>
               <Flex align="center" style={{width:160,margin:'10px'}} justify="space-evenly">
                   <div>
                        <Flex>
                           <span > <ContainerOutlined  style={{fontSize:20,marginRight:5}}/> </span>
                           <Tag color="blue">{userdata.vote}</Tag>
                        </Flex>
                   </div>
                   <div>
                        <Flex align="center">
                           <span> <LikeOutlined style={{fontSize:20,marginRight:5}}/> </span>
                           <Tag color="blue">{userdata.up}</Tag>
                        </Flex>
                   </div>
                   <div>
                        <Flex>
                           <span> <CommentOutlined style={{fontSize:20,marginRight:5}}/> </span>
                           <Tag color="blue">{userdata.comment}</Tag>
                        </Flex>
                   </div>
               </Flex>

               <Flex style={{marginLeft:13}}>
                   <span>유저 참여율</span>
                   <Progress percent={userdata.rate} size="large" style={{marginLeft:10,width:450}}/>
               </Flex>
            </div>
        </div>
        <div style={{width:'900px',margin:'0 auto',marginBottom:'20px'}}>
            <div style={{border:'1px solid',borderRadius:10,padding:10}}>
                <Tabs defaultActiveKey="1" centered>
                        <Tabs.TabPane tab="작성한 투표" key="1">
                             <MypageVoteWriteList />
                        </Tabs.TabPane>
                        <Tabs.TabPane tab="참여한 투표" key="2">
                             <MypageVoteSelectList />
                        </Tabs.TabPane>
                        <Tabs.TabPane tab="좋아요한 투표" key="3">
                              <MypageVoteLikeList />
                        </Tabs.TabPane>
                        <Tabs.TabPane tab="즐겨찾기한 투표" key="4">
                             <MypageVoteBookmarkList />
                        </Tabs.TabPane>
                </Tabs>
            </div>
        </div>
      </div>

   );

}

export default Mypage;