import React from 'react';
import { Card, Space,Flex, Progress, Slider, Typography,Avatar, List  } from 'antd';
import { ContainerOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
function Hotvote() {
        const votedata = [
          {
            title: '선택 1',
          },
          {
            title: '선택 2',
          },
        ];
        const voteList = [
            {id:0, title: 'ITZY 멤버 투표', percent1: 60, percent2: 40, dateStart: '2024.10.19', dateEnd: '2024.10.27' },
            {id:1, title: 'BTS 멤버 투표', percent1: 55, percent2: 45, dateStart: '2024.10.18', dateEnd: '2024.10.26' },
            {id:2, title: '블랙핑크 멤버 투표', percent1: 70, percent2: 30, dateStart: '2024.10.17', dateEnd: '2024.10.25' },
            {id:3, title: '엑소 멤버 투표', percent1: 65, percent2: 35, dateStart: '2024.10.16', dateEnd: '2024.10.24' },
            {id:4, title: '트와이스 멤버 투표', percent1: 80, percent2: 20, dateStart: '2024.10.15', dateEnd: '2024.10.23' },
            {id:5, title: '세븐틴 멤버 투표', percent1: 50, percent2: 50, dateStart: '2024.10.14', dateEnd: '2024.10.22' },
            {id:6, title: 'TXT 멤버 투표', percent1: 75, percent2: 25, dateStart: '2024.10.13', dateEnd: '2024.10.21' },
            {id:7, title: '뉴진스 멤버 투표', percent1: 40, percent2: 60, dateStart: '2024.10.12', dateEnd: '2024.10.20' },
            {id:8, title: '르세라핌 멤버 투표', percent1: 85, percent2: 15, dateStart: '2024.10.11', dateEnd: '2024.10.19' },
            {id:9, title: '스테이씨 멤버 투표', percent1: 45, percent2: 55, dateStart: '2024.10.10', dateEnd: '2024.10.18' },
          ];
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
                            {index + 1}
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
                                  percent={vote.percent1} // 각기 다른 데이터 기반 퍼센트 1
                                  steps={{ count: 10, gap: 10 }}
                                  format={() => `1`}
                                  trailColor="rgba(189, 189, 189)"
                                  strokeWidth={20}
                                  size={90}
                                />
                                <span style={{ fontSize: '30px', margin: '0 5px' }}> vs </span>
                                <Progress
                                  type="circle"
                                  percent={vote.percent2} // 각기 다른 데이터 기반 퍼센트 2
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
                                <span style={{ width: '60%' }}>{vote.percent1}%</span> {/* 투표율 표시 */}
                                <span>{vote.percent2}%</span>
                              </Flex>
                              <List
                                itemLayout="horizontal"
                                dataSource={[{ title: '1번 선택지',content:'선택1' }, { title: '2번 선택지',content:'선택2' }]}
                                renderItem={(item) => (
                                  <List.Item>
                                    <List.Item.Meta
                                      avatar={<ContainerOutlined />}
                                      title={
                                          <a>{item.title}</a>
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
                                <span>{vote.dateStart}</span> {/* 시작일 */}
                                <span style={{ margin: '0 5px' }}>~</span>
                                <span>{vote.dateEnd}</span> {/* 종료일 */}
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