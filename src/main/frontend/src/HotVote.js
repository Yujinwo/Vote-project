import React,{useEffect,useState} from 'react';
import { Card, Space,Flex, Progress, Slider, Typography,Avatar, List  } from 'antd';
import { ContainerOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import axios from 'axios'
function Hotvote() {
        const votedata = [
          {
            title: '선택 1',
          },
          {
            title: '선택 2',
          },
        ];
        const [voteList,setvoteList] = useState([]);
        useEffect(() => {
           axios.get('/api/votes/hot')
                 .then((res) => {
                      setvoteList(res.data.vote)
                 })

        },[])
        return (

              <div>
                   <Flex justify="center" align="center" style={{paddingBottom:'20px'}}>
                              <span style={{fontSize:'25px',fontWeight:'bold',border:'1px solid',borderRadius:'20px',padding:'20px 290px 20px',background:'#e6f4ff',opacity:'1',borderColor: '#91caff',color: '#1677ff'}}>인기 투표</span>
                   </Flex>
                   <>
                      {voteList.map((vote, index) => (
                      <Link to={`/votedetail/${vote.id}`} key={index}>
                        <Flex key={index} gap="small" wrap justify="center" align="center" style={{ marginTop: '20px' }}>
                          <span style={{ border: '1px solid', borderRadius: '10px', padding: '15px', fontSize: '20px', marginRight: '110px' }}>
                            {vote.rank}
                          </span>
                          <Space direction="vertical" size={16}>
                            <Card
                              title={vote.title} // 데이터 기반 제목 출력
                              style={{ width: 300}}
                            >
                              <Flex
                                wrap
                                style={{ margin: 16 }}
                                justify="center"
                                align="center"
                              >
                                <Progress
                                  type="circle"
                                  percent={vote.voteOptions[0].rate} // 각기 다른 데이터 기반 퍼센트 1
                                  steps={{ count: 10, gap: 10 }}
                                  format={() => `1`}
                                  trailColor="rgba(189, 189, 189)"
                                  strokeWidth={20}
                                  size={90}
                                />
                                <span style={{ fontSize: '30px', margin: '0 5px' }}> vs </span>
                                <Progress
                                  type="circle"
                                  percent={vote.voteOptions[1].rate} // 각기 다른 데이터 기반 퍼센트 2
                                  steps={{ count: 10, gap: 10 }}
                                  format={() => `2`}
                                  trailColor="rgba(189,189, 189)"
                                  strokeWidth={20}
                                  size={90}
                                />
                              </Flex>
                              <Flex
                                wrap
                                style={{ margin: 16, color: 'blue' }}
                                justify="center"
                                align="center"
                              >
                                <span style={{ width: '60%' }}>{vote.voteOptions[0].rate}%</span> {/* 투표율 표시 */}
                                <span>{vote.voteOptions[1].rate}%</span>
                              </Flex>
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
                              <Flex
                                wrap
                                style={{ margin: 16, color: '#be0000' }}
                                justify="center"
                                align="center"
                              >
                                <span>{vote.startDay}</span> {/* 시작일 */}
                                <span style={{ margin: '0 5px' }}>~</span>
                                <span>{vote.endDay}</span> {/* 종료일 */}
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