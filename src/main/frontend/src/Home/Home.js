import {useEffect, useState} from "react";
import axios from "axios";
import { Link,useNavigate  } from 'react-router-dom';
import { SearchOutlined } from '@ant-design/icons';
import '../App.css';
import ReactCardSlider from '../Card/ReactCardSlider';
import { Button, Divider, Flex, Radio, Space, Tooltip, Typography , Tag, Card,Input } from 'antd';
import { LikeOutlined } from '@ant-design/icons';
const { Title } = Typography;
const { Search } = Input;
function Home() {
    const [voteTotal, setvoteTotal] = useState(0);
    const [hotCategory, sethotCategory] = useState('');
    const [slides,setslides] = useState([]);
    const navigate = useNavigate();

    const styles =
    {
          headerBox: {
            width: '700px',
            margin: '0 auto',
            marginTop: '100px',
            padding: '20px',
            textAlign: 'center',
          },
          headerButton: {
             width: '130px',
             padding: '20px',
             textAlign: 'center',
          },
          searchBox: {
            padding: '20px',
            textAlign: 'center',
          },
          searchButton: {
            width: '100%',
            padding: '40px',
            textAlign: 'center',
          },
          contentBox: {
            width: '1000px',
            margin: '0 auto',
            padding: '20px',
            textAlign: 'center',
          },
          contentCountBox: {
            width: '300px',
            margin:'0 20px',
            border: '1px solid',
            borderRadius: '10px'
          },
          cardBox: {
            width: '1200px',
            margin: '0 auto',
            marginTop: '100px',
          },
     };

    useEffect(() =>
    {
      // 총 투표 수 와 인기 카테고리 조회
      axios.get('/api/votes/summary')
        .then((res) => {
               sethotCategory(res.data.category)
               setvoteTotal(res.data.total)
      })
      // 투표 추천 리스트 조회
      axios.get('/api/votes/recommend')
        .then((res) => {
               const newSlides = res.data.vote.map((v) =>
               ({
                     id: v.id,
                     category: v.category,
                     title: v.title,
                     startDay: v.startDay,
                     endDay: v.endDay,
                     writer: v.user.user_nick,
                     rate: v.optionCountTotal,
                     up: v.up,
                     commentCount: v.commentCount,
               }));
               setslides(newSlides);
      })
    }, []);

    function onSearch(value)
    {
           // 검색 키워드와 함께 투표 참여 페이지로 이동
           navigate('/votelist',
           {
                 state: { value: value }
           });
    }
    return (
         <div>
             <div style={styles.headerBox}>
               <Flex gap="small" wrap justify="space-evenly" align="center">
                 {/* 유저 참여율 통계 버튼 */}
                 <Link to="/voterate">
                   <Button style={styles.headerButton}  color="default" variant="outlined">유저 참여율 통계</Button>
                 </Link>
                 {/* 인기 투표 버튼 */}
                 <Link to="/hotvote">
                   <Button style={styles.headerButton}  color="default" variant="outlined">인기 투표</Button>
                 </Link>
                 {/* 투표 참여 버튼 */}
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
                     {/* 총 투표 수 */}
                     <Flex gap="small" wrap justify="center" align="center">
                       <div style={styles.contentCountBox} >
                           <Divider>총 투표 수</Divider>
                           <p>
                             <Tag  color="processing">
                                     {voteTotal}
                             </Tag>
                           </p>
                       </div>
                       {/* 인기 카테고리 */}
                       <div style={styles.contentCountBox} >
                           <Divider>인기 카테고리</Divider>
                           <p>
                              <Tag color="processing">
                                   {hotCategory}
                              </Tag>
                           </p>
                       </div>
                     </Flex>
                </div>
            </div>
            {/* 투표 추천 리스트 */}
            <div style={styles.cardBox}>
                <Title color="primary" level={3}>투표 추천</Title>
                <ReactCardSlider slides={slides} />
            </div>
         </div>
    );
}

export default Home;