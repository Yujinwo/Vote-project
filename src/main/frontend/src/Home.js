import {useEffect, useState} from "react";
import axios from "axios";
import { Link } from 'react-router-dom';
import { SearchOutlined } from '@ant-design/icons';
import './App.css';  // 스타일 파일을 임포트
import ReactCardSlider from './ReactCardSlider';
import { Button, Divider, Flex, Radio, Space, Tooltip, Typography , Tag, Card,Input } from 'antd';
import { LikeOutlined } from '@ant-design/icons';
const { Title } = Typography;
const { Search } = Input;
function Home() {
    const [hello, setHello] = useState('');
    const slides = [
        {id:0,category:"음식",title:"주로 먹방을 즐겨 보시나요?",staryday:"2024.10.17",endday:"2024.10.30",writer:"작성자",rate:"1000 ",up:10,commentcount:10 },
        {id:1,category:"패션",title:"아이돌 걸그룹 랭킹 누가 일등? 2019년",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1002 ",up:10,commentcount:10},
        {id:2,category:"음식",title:"ITZY 있지 멤버 인기순위 투표해주세요! 내가 제일 좋아하는 멤버에 투표!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"10 ",up:10,commentcount:10},
        {id:3,category:"엔터테인먼트",title:"트와이스 멤버 인기 순위 투표 가장 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"100 ",up:10,commentcount:10},
        {id:4,category:"애완동물",title:"문재인 대통령의 전반적인 국정운영에 만족하십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1200 ",up:10,commentcount:10},
        {id:5,category:"영화",title:"조국 법무부 장관 임명이 적절하다고 보십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1030 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
        {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
    ]
    const styles = {
          headerBox: {
            width: '700px',
            margin: '0 auto', // 좌우 중앙 정렬
            marginTop: '100px',
            padding: '20px',
            textAlign: 'center', // 텍스트 중앙 정렬
          },
          headerButton: {
             width: '130px',
             padding: '20px',
             textAlign: 'center', // 텍스트 중앙 정렬
          },
          searchBox: {
            padding: '20px',
            textAlign: 'center', // 텍스트 중앙 정렬
          },
          searchButton: {
            width: '100%',
            padding: '40px',
            textAlign: 'center', // 텍스트 중앙 정렬
          },
          contentBox: {
            width: '1000px',
            margin: '0 auto', // 좌우 중앙 정렬
            padding: '20px',
            textAlign: 'center', // 텍스트 중앙 정렬
          },
          contentCountBox: {
              width: '300px',
              margin:'0 20px',
              border: '1px solid',
              borderRadius: '10px'
          },
          cardBox: {
          width: '1200px',
          margin: '0 auto', // 좌우 중앙 정렬
          marginTop: '100px',
          },

     };

    useEffect(() => {

    }, []);

    function onSearch(value) {
            console.log(value)
    }
    return (
         <div>
             <div style={styles.headerBox}>
               <Flex gap="small" wrap justify="space-evenly" align="center">
                 <Link to="/voterate">
                   <Button style={styles.headerButton}  color="default" variant="outlined">유저 참여율 통계</Button>
                 </Link>
                 <Link to="/hotvote">
                   <Button style={styles.headerButton}  color="default" variant="outlined">인기 투표</Button>
                 </Link>
                 <Link to="/votelist">
                   <Button style={styles.headerButton}  color="default" variant="outlined">투표 참여</Button>
                 </Link>
                </Flex>
                <div style={styles.searchBox}>
                <Search
                      placeholder="검색을 통해 다양한 투표를 참여해보세요!"
                      allowClear
                      size="large"
                      enterButton
                      onSearch={onSearch}
                    />
                </div>
            </div>
            <div style={styles.contentBox}>
                <div>
                     <Flex gap="small" wrap justify="center" align="center">
                       <div style={styles.contentCountBox} >
                           <Divider>총 투표 수</Divider>
                           <p>
                             <Tag  color="processing">
                                     100
                             </Tag>
                           </p>
                       </div>
                       <div style={styles.contentCountBox} >
                           <Divider>인기 카테고리</Divider>
                           <p>
                              <Tag color="processing">
                                    음식 및 요리
                              </Tag>
                           </p>
                       </div>
                     </Flex>
                </div>
            </div>

            <div style={styles.cardBox}>
                <Title color="primary" level={3}>투표 추천</Title>
                <ReactCardSlider slides={slides} />
            </div>
         </div>
    );
}

export default Home;