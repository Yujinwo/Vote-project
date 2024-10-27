
import {Input,Typography,Button,Flex} from 'antd'
import React, { useState } from 'react';
import ReactCardSlider from './ReactCardSlider';
import { EditOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { useAuth } from './AuthContext';
const { Search } = Input;
const { Title } = Typography;

function VoteListAll() {
    const { isLoggedIn, logout,login  } = useAuth();
    const slides = [
            {id:0,category:"음식",title:"주로 먹방을 즐겨 보시나요?",staryday:"2024.10.17",endday:"2024.10.30",writer:"작성자",rate:"1000 ",up:10,commentcount:10},
            {id:1,category:"패션",title:"아이돌 걸그룹 랭킹 누가 일등? 2019년",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1002 ",up:10,commentcount:10},
            {id:2,category:"음식",title:"ITZY 있지 멤버 인기순위 투표해주세요! 내가 제일 좋아하는 멤버에 투표!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"10 ",up:10,commentcount:10},
            {id:3,category:"엔터테인먼트",title:"트와이스 멤버 인기 순위 투표 가장 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"100 ",up:10,commentcount:10},
            {id:4,category:"애완동물",title:"문재인 대통령의 전반적인 국정운영에 만족하십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1200 ",up:10,commentcount:10},
            {id:5,category:"영화",title:"조국 법무부 장관 임명이 적절하다고 보십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1030 ",up:10,commentcount:10},
            {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
    ]
    const styles = {
        cardBox: {
                  width: '1200px',
                  margin: '0 auto', // 좌우 중앙 정렬
                  marginTop: '100px',
        },

    }
    function onSearch(value) {
                console.log(value)
    }
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
                              <Title color="primary" level={3}>투표 참여</Title>
                              <ReactCardSlider slides={slides}/>
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