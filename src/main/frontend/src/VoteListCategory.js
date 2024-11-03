
import {Input,Typography,Flex,Select,Button} from 'antd'
import ReactCardSlider from './ReactCardSlider';
import React, { useState } from 'react';
import { EditOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { useAuth } from './AuthContext';
const { Search } = Input;
const { Title } = Typography;
function VoteListCategory() {
     const { isLoggedIn, logout,login  } = useAuth();
     const categorys = [
                       {
                           value: 'ENTERTAINMENT',
                           label: '엔터테인먼트',
                       },
                       {
                           value: 'SPORTS',
                           label: '스포츠',
                       },
                       {
                          value: 'FASHION_BEAUTY',
                          label: '패션 및 부티',
                       },
                       {
                          value: 'FOOD_CULINARY',
                          label: '음식 및 요리',
                       },
                       {
                          value: 'LIFESTYLE',
                          label: '라이프스타일',
                       },
                       {
                          value: 'GAMING_IT',
                          label: '게임 및 IT',
                       },
                       {
                          value: 'EDUCATION_LEARNING',
                          label: '교육 및 학습',
                       },
     ]
     const orders = [
                        {
                                value: 'Newest',
                                label: '최신',
                        },
                        {
                                value: 'UserCount',
                                label: '참여율',
                        },
                        {
                                value: 'Like',
                                label: '좋아요',
                        },
     ]
     const slides = [
                 {id:0,category:"음식",title:"주로 먹방을 즐겨 보시나요?",staryday:"2024.10.17",endday:"2024.10.30",writer:"작성자",rate:"1000 ",up:10,commentcount:10},
                 {id:1,category:"패션",title:"아이돌 걸그룹 랭킹 누가 일등? 2019년",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1002 ",up:10,commentcount:10},
                 {id:2,category:"음식",title:"ITZY 있지 멤버 인기순위 투표해주세요! 내가 제일 좋아하는 멤버에 투표!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"10 ",up:10,commentcount:10},
                 {id:3,category:"엔터테인먼트",title:"트와이스 멤버 인기 순위 투표 가장 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"100 ",up:10,commentcount:10},
                 {id:4,category:"애완동물",title:"문재인 대통령의 전반적인 국정운영에 만족하십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1200 ",up:10,commentcount:10},
                 {id:5,category:"영화",title:"조국 법무부 장관 임명이 적절하다고 보십니까?",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1030 ",up:10,commentcount:10},
                 {id:6,category:"뮤비",title:"체리블렛 멤버 인기순위. 제일 좋아하는 멤버에 투표해주세요!",staryday:"2024.10.17",endday:"2024.10.30",writer:"테스트",rate:"1020 ",up:10,commentcount:10},
     ]
     const [OrderValue, setOrderValue] = useState('Newest'); // 초기값 'a'
     const [CategoryValue, setCategoryValue] = useState(null);
     const handleChangecategory = (value) => {
              setCategoryValue(value); // 선택된 값 저장
              console.log('Selected category:', value); // 선택된 값 콘솔에 출력
     };
     const handleChangeorder = (value) => {
              setOrderValue(value); // 선택된 값 저장
              console.log('Selected order:', value); // 선택된 값 콘솔에 출력
     };
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
                                       enterButton
                                       size="large"
                                       onSearch={onSearch}
                 />
             </div>
             <div style={styles.cardBox}>
                        <Flex style={{marginBottom:'10px'}}justify="space-between" align="center">
                              <Select
                                      showSearch
                                      placeholder="카테고리 선택"
                                      onChange={handleChangecategory} // 선택된 값을 가져오는 핸들러
                                      filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                      }
                                      options={categorys}
                                      size="large"
                              />
                              <Select
                                      showSearch
                                      placeholder="정렬 선택"
                                      onChange={handleChangeorder} // 선택된 값을 가져오는 핸들러
                                      filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                      }
                                      options={orders}
                                      size="large"
                              />
                        </Flex>
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

export default VoteListCategory;