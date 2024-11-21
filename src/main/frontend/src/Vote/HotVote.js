import React,{useEffect,useState} from 'react';
import { Card, Space,Flex, Progress, Slider, Typography,Avatar, List  } from 'antd';
import { ContainerOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import axios from  "../apiClient"
function Hotvote() {
        const [voteList,setvoteList] = useState([]);
        useEffect(() =>
        {
           axios.get('/api/votes/hot')
                 .then((res) =>
                 {
                      setvoteList(res.data.vote)
                 })
        },[])
        return (
              <div>
                   {/* 인기 투표 텍스트 */}
                   <Flex justify="center" align="center" style={{paddingBottom:'20px'}}>
                       <span style={{fontSize:'25px',fontWeight:'bold',border:'1px solid',borderRadius:'20px',padding:'20px 290px 20px',background:'#e6f4ff',opacity:'1',borderColor: '#91caff',color: '#1677ff'}}>인기 투표</span>
                   </Flex>
                   <>
                      {/* 인기 투표 리스트 순차 로딩 */}
                      {voteList.map((vote, index) => (
                      <Link to={`/votedetail/${vote.id}`} key={index}>
                        <Flex key={index} gap="small" wrap justify="center" align="center" style={{ marginTop: '20px' }}>
                          {/* 투표 랭킹 */}
                          <span style={{ border: '1px solid', borderRadius: '10px', padding: '15px', fontSize: '20px', marginRight: '110px' }}>
                            {vote.rank}
                          </span>
                          <Space direction="vertical" size={16}>
                            {/* 투표 제목 */}
                            <Card
                              title={vote.title}
                              style={{ width: 300}}
                            >
                              <Flex
                                wrap
                                style={{ margin: 16 }}
                                justify="center"
                                align="center"
                              >
                                {/* 선택지 1 투표 참여율 퍼센트 표시 */}
                                <Progress
                                  type="circle"
                                  percent={vote.voteOptions[0].rate}
                                  steps={{ count: 10, gap: 10 }}
                                  format={() => `1`}
                                  trailColor="rgba(189, 189, 189)"
                                  strokeWidth={20}
                                  size={90}
                                />
                                <span style={{ fontSize: '30px', margin: '0 5px' }}> vs </span>
                                {/* 선택지 2 투표 참여율 퍼센트 표시 */}
                                <Progress
                                  type="circle"
                                  percent={vote.voteOptions[1].rate}
                                  steps={{ count: 10, gap: 10 }}
                                  format={() => `2`}
                                  trailColor="rgba(189,189, 189)"
                                  strokeWidth={20}
                                  size={90}
                                />
                              </Flex>
                              {/* 투표율 텍스트 */}
                              <Flex
                                wrap
                                style={{ margin: 16, color: 'blue' }}
                                justify="center"
                                align="center"
                              >
                                <span style={{ width: '60%' }}>{vote.voteOptions[0].rate}%</span>
                                <span>{vote.voteOptions[1].rate}%</span>
                              </Flex>
                              {/* 선택지 텍스트 */}
                              <List
                                itemLayout="horizontal"
                                dataSource={vote.voteOptions}
                                renderItem={(item,index) => (
                                  <List.Item>
                                    <List.Item.Meta
                                      avatar={<ContainerOutlined />}
                                      title={
                                          <a>{index + 1}번 선택지</a>
                                      }
                                      description={item.content}
                                    />
                                  </List.Item>
                                )}
                              />
                              {/* 기간 */}
                              <Flex
                                wrap
                                style={{ margin: 16, color: '#be0000' }}
                                justify="center"
                                align="center"
                              >
                                {/* 시작일 */}
                                <span>{vote.startDay}</span>
                                <span style={{ margin: '0 5px' }}>~</span>
                                {/* 종료일 */}
                                <span>{vote.endDay}</span>
                              </Flex>
                            </Card>
                          </Space>
                        </Flex>
                       </Link>
                      ))}
                   </>
              </div>
        );
}

export default Hotvote;