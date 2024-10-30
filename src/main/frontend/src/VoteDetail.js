import React, { useState,useEffect } from 'react';
import { Card, Space,Flex, Progress, List,Button, Dropdown, Menu,Input,message } from 'antd';
import { MenuOutlined } from '@ant-design/icons';
import { useParams,Link } from 'react-router-dom';
import { LikeOutlined,CommentOutlined,BookOutlined} from '@ant-design/icons';
import { useAuth } from './AuthContext';
import axios from 'axios'
function VoteDetail() {
            const { id } = useParams();
            const { isLoggedIn,userid } = useAuth();

            // 선택지 데이터
            const [options, setoptions] = useState([
                                     { id: 1, label: 'Sample1', percent: 10 },
                                     { id: 2, label: 'Sample2', percent: 10 },
            ]);

            // 댓글 데이터
            const [comments,setcomments] = useState([])
            const [commentPage,setcommentPage] = useState(1);
            const [hasNext,sethasNext] = useState(false);

            // 투표 기본 데이터
            const [voteNormaldata,setvoteNormaldata] = useState('')
            const [selectedOption, setSelectedOption] = useState(null);

            const voteRenderingData = () => {
                 axios.get('/api/votes?id=' + id)
                                              .then((res) => {
                                                  setcommentPage(1)
                                                  const vote = res.data.vote;
                                                  // 선택지 설정
                                                  setoptions([
                                                   { id: vote.voteOptions[0].id, label: vote.voteOptions[0].content, percent: vote.voteOptions[0].rate },
                                                   { id: vote.voteOptions[1].id, label: vote.voteOptions[1].content, percent: vote.voteOptions[1].rate },
                                                  ])
                                                  // 투표 기본 데이터 설정
                                                  setvoteNormaldata(
                                                  { title: vote.title, category:vote.category,up:vote.up,commentCount:vote.commentCount}
                                                  )
                                                  // 선택한 투표 데이터 동기화
                                                  setSelectedOption(res.data.selectedOptionId);

                                                  // 댓글 데이터 설정
                                                  setDataCount(res.data.comments.length)
                                                  setcomments(res.data.comments)
                                                  setVisibleData(res.data.comments.slice(0,dataCount))

                                                  // 댓글 더보기 활성화 설정
                                                  sethasNext(res.data.hasNext);
                 })

            }


             useEffect(() => {
                        voteRenderingData();
             }, []);
            const [editingCommentId, setEditingCommentId] = useState(null); // 수정 중인 댓글 ID
            const [initialData,setinitialData] = useState('');
            const [updatedComment, setUpdatedComment] = useState(''); // 수정된 댓글 내용
            const [createdComment, setcreatedComment] = useState(''); // 수정된 댓글 내용


            const [commentIsExceeded,setcommentIsExceeded] = useState(null);

            const CommentEditClick = (commentId, commentText) => {
                setEditingCommentId(commentId); // 수정 중인 댓글 ID 설정
                setUpdatedComment(commentText);
                setinitialData(commentText); // 기존 댓글 내용을 수정 필드에 세팅
            };
            const CommentSaveClick = () => {
               if(updatedComment == initialData) {
                   message.error('기존 내용과 동일합니다.')
               }
               // 댓글 수정 APi 호출
               else {

                    axios.patch('/api/comments',{
                                               vote_id:id,
                                               comment_id:editingCommentId,
                                               content:updatedComment
                                            },{
                                               headers:
                                               {
                                                    'Content-Type': 'application/json'
                                               }
                    })
                    .then((res) => {
                      voteRenderingData();
                      message.success(res.data.result);
                      setEditingCommentId(null); // 수정 모드 종료
                      setUpdatedComment(''); // 수정 필드 초기화
                    })
                    .catch((err) => {
                      message.error(err.response.data.result);
                      setEditingCommentId(null); // 수정 모드 종료
                      setUpdatedComment(''); // 수정 필드 초기화
                    })


               }
               //onUpdate(editingCommentId, updatedComment); // 수정된 댓글 저장

            };
            const CommentCancelClick = () => {
               setEditingCommentId(null); // 수정 모드 종료
               setUpdatedComment(''); // 수정 필드 초기화
            };
            const CommentDeleteClick = (commentId) => {
               axios.delete('/api/comments?id=' + commentId)
               .then((res) => {
                         message.success(res.data.result);
                         voteRenderingData();
               })
               .catch((err) => {
                         message.error(err.response.data.result);
               });
            };

            const VoteDeleteClick = () => {
                console.log(id);
            }
            const VoteMenu = (
                <Menu>
                  <Link to= {`/voteupdate/${id}`}>
                      <Menu.Item key="1">수정</Menu.Item>
                  </Link>
                  <Menu.Item key="2" onClick={VoteDeleteClick}>삭제</Menu.Item>
                </Menu>
              );
            const CommentMenu = (commentId,commentText) => (
                  <Menu>
                      <Menu.Item key="1" onClick={() => CommentEditClick(commentId, commentText)}>수정</Menu.Item>
                      <Menu.Item key="2" onClick={() => CommentDeleteClick(commentId)}>삭제</Menu.Item>
                  </Menu>
            );

            const handleSelect = (id) => {
                  if(selectedOption != id)
                  {
                       setSelectedOption(id); // 선택된 항목 업데이트
                       axios.post('/api/voteoptions?id=' + id)
                                   .then((res) => {
                                        message.success(res.data.result);
                                    })
                                   .catch((err) => {
                                        message.error(err.response.data.result)
                                   })
                  }
            };
            const [visibleData, setVisibleData] = useState(comments.slice(0,9)); // 초기 10개 데이터
            const [dataCount, setDataCount] = useState(10); // 현재 표시된 데이터 개수

            const handleLoadMore = () => {
                  const page = commentPage + 1
                  axios.get('/api/comments?id=' + id + '&page=' + page)
                        .then((res) => {
                             if(res.data.hasContent)
                             {

                                          setcommentPage(page)
                                          const newComments = res.data.comment;
                                          const updatedComments = [...comments, ...newComments];
                                          setcomments(updatedComments); // 기존 데이터에 추가

                                          const nextDataCount = dataCount + res.data.comment.length;
                                          setVisibleData(updatedComments.slice(0, nextDataCount));
                                          setDataCount(nextDataCount);
                             }
                             sethasNext(res.data.hasNext);

                        })


            };

            const handleChange = (e) => {
                 const minLength = 3;
                 const maxLength = 280;
                 const value = e.target.value;
                 setcreatedComment(value)
                 if( (value.length < minLength || value.length > maxLength )){
                        setcommentIsExceeded(true);
                        message.error("최소 3자 이상, 최대 280자 이하로 입력해주세요");
                 }
                 else if (value === null || value.trim() === '') {
                        setcommentIsExceeded(true);
                        message.error("내용을 입력해주세요");
                 }
                 else {
                        setcommentIsExceeded(false);
                 }
            }
            const commentSubmit = () => {
                 if(commentIsExceeded) {
                    message.error('입력을 확인해주세요');
                 }
                 else if (!isLoggedIn){
                    message.error('로그인을 해주세요');
                 }
                 else {
                    axios.post('/api/comments', {
                             vote_id:id,
                             content:createdComment
                             }, {
                             headers: {
                                      'Content-Type': 'application/json'
                             }
                          })
                          .then((res) => {
                               message.success(res.data.result);
                               voteRenderingData();
                          })
                          .catch((err) => {
                               message.error(err.response.data.result);
                          })


                 }
            }
            return (
                 <div>
                      <Flex gap="small" wrap justify="center" align="center" style={{ marginTop: '20px' }}>
                          <Space direction="vertical" size={16}>
                            <Card
                              title={voteNormaldata.title} // 데이터 기반 제목 출력
                              extra={

                                  <Dropdown overlay={VoteMenu} trigger={['click']}>
                                       <Button type="primary">
                                         <MenuOutlined />
                                       </Button>
                                     </Dropdown>
                              }
                              style={{ width: 500}}
                            >
                              {options.map((option) => (
                                                          <Card
                                                            key={option.id}
                                                            style={{
                                                              marginBottom: 16,
                                                              margin: '0 auto',
                                                              width: '450px',
                                                              backgroundColor: selectedOption === option.id ? '#e6f7ff' : '#fff',
                                                            }}
                                                          >
                                                            <Button
                                                              type={selectedOption === option.id ? 'primary' : 'default'}
                                                              onClick={() => handleSelect(option.id)}
                                                              style={{ width: '100%' }}
                                                            >
                                                              {option.label}
                                                            </Button>
                                                            <Progress
                                                              percent={option.percent}
                                                              status="active"
                                                              format={(percent) => `${percent}%`} // 선택지와 퍼센트 표시
                                                            />
                                                          </Card>
                              ))}
                              <Flex justify="end" align="center">
                                        <div style={{marginTop:10}}>
                                               <Button>
                                                   <LikeOutlined />
                                                   <span> {voteNormaldata.up} </span>
                                               </Button>

                                               <Button>
                                                   <CommentOutlined style={{}}/>
                                                    <span> {voteNormaldata.commentCount} </span>
                                               </Button>

                                               <Button>
                                                   <BookOutlined style={{fontSize:20}}/>
                                               </Button>
                                       </div>
                              </Flex>

                            </Card>
                             <Space.Compact
                                   style={{
                                     width: '100%',
                                     height:100
                                   }}
                                   >
                                   <Input placeholder="댓글을 작성해보세요" count={{show:true,max:380}} onBlur={handleChange}/>
                                   <Button style={{height:100}}type="primary" onClick={commentSubmit}>작성</Button>
                                 </Space.Compact>

                            <Card style={{ width: 500}}>
                                       {visibleData.map((comment) => (
                                          <div style={{border:'1px solid',borderRadius:'10px',marginTop:'10px',background:'#f8fafc',padding:'10px'}}>
                                              <Flex justify="space-between">
                                                  <div>
                                                       <span>{comment.user.user_nick}</span>
                                                       <span>({comment.user.user_id})</span>
                                                       <span style={{marginLeft:10,fontSize:11}}>* {comment.created_date}</span>
                                                  </div>
                                                  { comment.user.user_id == userid ? (
                                                  <div>
                                                       <Dropdown overlay={CommentMenu(comment.id,comment.content)} trigger={['click']}>
                                                           <Button type="primary">
                                                                <MenuOutlined />
                                                           </Button>
                                                       </Dropdown>
                                                  </div> ) : ''
                                                  }
                                              </Flex>
                                              { editingCommentId == comment.id ? (
                                                      <div>
                                                        <Input.TextArea
                                                          onBlur={(e) => setUpdatedComment(e.target.value)}
                                                          rows={2}
                                                          style={{marginTop:10}}
                                                          size="large"
                                                        />
                                                        <Flex justify="space-between" style={{marginTop:10}}>
                                                            <Button type="primary" onClick={CommentSaveClick}>
                                                               저장
                                                            </Button>
                                                            <Button type="primary" onClick={CommentCancelClick}>
                                                               취소
                                                            </Button>
                                                        </Flex>
                                                      </div>
                                                 )
                                              :  ( <p style={{color:'#aaa'}}>{comment.content}</p> )

                                              }

                                          </div>
                                       ))}
                                       {hasNext && (
                                          <div className="button-container"> {/* 가운데 정렬을 위한 버튼 컨테이너 */}
                                                  <Button onClick={handleLoadMore}>
                                                           더보기
                                                  </Button>
                                           </div>
                                       )}
                            </Card>
                          </Space>
                      </Flex>
                 </div>
            );
}

export default VoteDetail;