import React, { useState,useEffect } from 'react';
import { Card, Space,Flex, Progress, List,Button, Dropdown, Menu,Input,message } from 'antd';
import { MenuOutlined } from '@ant-design/icons';
import { useParams,Link,useNavigate } from 'react-router-dom';
import { LikeOutlined,LikeFilled,CommentOutlined,BookOutlined,BookFilled} from '@ant-design/icons';
import { useAuth } from '../Auth/AuthContext';
import axios from 'axios'
function VoteDetail() {
            const { id } = useParams();
            const { isLoggedIn,userid } = useAuth();
            const navigate = useNavigate();

            // 선택지 데이터
            const [options, setoptions] = useState([]);

            // 투표 유저 정보
            const [vote_userId,setuserId] = useState(null)

            // 투표 선택지 정보
            const [optionCountTotal,setoptionCountTotal] = useState(0);

            // 댓글 데이터
            const [comments,setcomments] = useState([])
            const [commentPage,setcommentPage] = useState(1);
            const [hasNext,sethasNext] = useState(false);
            const [commentCount,setcommentCount] = useState(0);
            const [up,setup] = useState(0);
            const [editingCommentId, setEditingCommentId] = useState(null);
            const [initialData,setinitialData] = useState('');
            const [updatedComment, setUpdatedComment] = useState('');
            const [createdComment, setcreatedComment] = useState('');
            const [commentIsExceeded,setcommentIsExceeded] = useState(null);

            // 투표 기본 데이터
            const [voteNormaldata,setvoteNormaldata] = useState('')
            const [selectedOption, setSelectedOption] = useState(null);
            const [hasUp,sethasUp] = useState(false);
            const [hasBookmark,sethasBookmark] = useState(false);

            const [visibleData, setVisibleData] = useState(comments.slice(0,9)); // 초기 10개 데이터
            const [dataCount, setDataCount] = useState(10); // 현재 표시된 데이터 개수

            const voteRenderingData = () =>
            {
                 axios.get('/api/votes?id=' + id)
                   .then((res) =>
                   {
                        if(res.data.vote == null)
                        {
                               navigate('/votelist');
                               message.error('데이터가 존재하지 않습니다');
                        }
                        else
                        {
                               setcommentPage(1)
                               const vote = res.data.vote;

                               setuserId(vote.user.user_id)
                               // 선택지 설정
                               setoptions([
                                   { id: vote.voteOptions[0].id, label: vote.voteOptions[0].content, percent: vote.voteOptions[0].rate, userCountTotal: vote.voteOptions[0].userCountTotal },
                                  { id: vote.voteOptions[1].id, label: vote.voteOptions[1].content, percent: vote.voteOptions[1].rate, userCountTotal: vote.voteOptions[1].userCountTotal },
                               ])

                               // 투표 기본 데이터 설정
                               setvoteNormaldata({ title: vote.title, category:vote.category,up:vote.up,commentCount:vote.commentCount})

                               // 투표 좋아요,댓글 수
                               setcommentCount(vote.commentCount)
                               setup(vote.up)

                                // 선택한 투표 데이터 동기화
                               setSelectedOption(res.data.selectedOptionId);

                               // 댓글 데이터 설정
                               setDataCount(res.data.comments.length)
                               setcomments(res.data.comments)
                               setVisibleData(res.data.comments)

                               // 댓글 더보기 활성화 설정
                               sethasNext(res.data.hasNext);

                               // 좋아요 활성화 설정
                               sethasUp(res.data.hasUp);

                               // 북마크 활성화 설정
                               sethasBookmark(res.data.hasBookMark);

                               // 투표 선택지 정보 백업
                               setoptionCountTotal(vote.optionCountTotal);
                        }
                   })
            }
            useEffect(() =>
            {
                 voteRenderingData();
            }, []);


            const CommentEditClick = (commentId, commentText) =>
            {
                setEditingCommentId(commentId);
                setUpdatedComment(commentText);
                setinitialData(commentText);
            };

            const CommentSaveClick = () =>
            {
               if(updatedComment == initialData) {
                   message.error('기존 내용과 동일합니다.')
               }
               else
               {
                    axios.patch('/api/comments',
                    {
                            vote_id:id,
                            comment_id:editingCommentId,
                            content:updatedComment
                    },
                    {
                            headers:
                            {
                                'Content-Type': 'application/json'
                            }
                    })
                    .then((res) =>
                    {
                            voteRenderingData();
                            message.success(res.data.result);
                            setEditingCommentId(null);
                            setUpdatedComment('');
                    })
                    .catch((err) =>
                    {
                            message.error(err.response.data.result);
                            setEditingCommentId(null);
                            setUpdatedComment('');
                    })
               }
            };

            const CommentCancelClick = () =>
            {
                   setEditingCommentId(null);
                   setUpdatedComment('');
            };

            const CommentDeleteClick = (commentId) =>
            {
               axios.delete('/api/comments?id=' + commentId)
                 .then((res) =>
                 {
                         message.success(res.data.result);
                         voteRenderingData();
                 })
                 .catch((err) =>
                 {
                         message.error(err.response.data.result);
                 });
            };

            const VoteDeleteClick = () =>
            {
                axios.delete("/api/votes?id=" + id)
                 .then((res) =>
                 {
                      message.success(res.data.result);
                      navigate('/votelist');
                 })
                 .catch((err) =>
                 {
                      message.error(err.response.data.result);
                 })
            }
            // 투표 더보기 메뉴
            const VoteMenu = (
                <Menu>
                      <Link to= {`/voteupdate/${id}`}>
                          <Menu.Item key="1">수정</Menu.Item>
                      </Link>
                      <Menu.Item key="2" onClick={VoteDeleteClick}>삭제</Menu.Item>
                </Menu>
            );

            // 댓글 더보기 메뉴
            const CommentMenu = (commentId,commentText) => (
                 <Menu>
                      <Menu.Item key="1" onClick={() => CommentEditClick(commentId, commentText)}>수정</Menu.Item>
                      <Menu.Item key="2" onClick={() => CommentDeleteClick(commentId)}>삭제</Menu.Item>
                 </Menu>
            );

            // 선택 데이터 수정
            const selectRendering = (id) => {
                  if(selectedOption != id) {
                        if(selectedOption == null) {
                               setSelectedOption(id);
                               setoptions(prevOptions =>
                                            prevOptions.map(option =>
                                                option.id === id ? {  ...option, userCountTotal: (option.userCountTotal + 1) } : option
                                            )
                               )
                               setoptionCountTotal(optionCountTotal + 1)

                               setoptions(prevOptions =>
                                            prevOptions.map(option =>
                                                option.id == id ? {  ...option, percent: ( (option.userCountTotal + 1) / (optionCountTotal + 1 )) * 100 } : {  ...option, percent: ( (option.userCountTotal) / (optionCountTotal + 1 )) * 100 }
                                            )
                               )
                        }
                        else {

                               setSelectedOption(id);
                               setoptions(prevOptions =>
                                            prevOptions.map(option =>
                                                option.id == id ? {  ...option, userCountTotal: (option.userCountTotal + 1) } : {  ...option, userCountTotal: (option.userCountTotal - 1) }
                                            )
                               )
                               setoptions(prevOptions =>
                                            prevOptions.map(option =>
                                                option.id == id ? {  ...option, percent: ( (option.userCountTotal + 1) / optionCountTotal) * 100 } : {  ...option, percent: ( (option.userCountTotal - 1) / optionCountTotal) * 100 }
                                            )
                               )
                        }
                }
            };
            // 투표 선택 함수
            const handleSelect = (id) =>
            {
                axios.post('/api/voteoptions?id=' + id)
                   .then((res) =>
                   {
                            selectRendering(id)
                            message.success(res.data.result);
                   })
                   .catch((err) =>
                   {
                            message.error(err.response.data.result)
                   })
            }
            // 댓글 더보기 함수
            const handleLoadMore = () => {
                  const page = commentPage + 1
                  axios.get('/api/comments?id=' + id + '&page=' + page)
                        .then((res) =>
                        {
                             if(res.data.hasContent)
                             {
                                   setcommentPage(page)

                                   const newComments = res.data.comment;
                                   const updatedComments = [...comments, ...newComments];
                                   setcomments(updatedComments); // 기존 데이터에 추가

                                   const nextDataCount = dataCount + res.data.comment.length;
                                   setVisibleData(updatedComments.slice(0, nextDataCount));
                                   setDataCount(nextDataCount);

                                   setcommentCount(res.data.total)
                             }
                             sethasNext(res.data.hasNext);
                        })
            };

            // 댓글 수정 함수
            const handleChange = (e) =>
            {
                 const minLength = 3;
                 const maxLength = 280;
                 const value = e.target.value;
                 setcreatedComment(value)
                 if( (value.length < minLength || value.length > maxLength ))
                 {
                        setcommentIsExceeded(true);
                        message.error("최소 3자 이상, 최대 280자 이하로 입력해주세요");
                 }
                 else if (value === null || value.trim() === '')
                 {
                        setcommentIsExceeded(true);
                        message.error("내용을 입력해주세요");
                 }
                 else {
                        setcommentIsExceeded(false);
                 }
            }
            // 댓글 작성 함수
            const commentSubmit = () =>
            {
                 if(commentIsExceeded)
                 {
                    message.error('입력을 확인해주세요');
                 }
                 else if (!isLoggedIn)
                 {
                    message.error('로그인을 해주세요');
                 }
                 else
                 {
                    axios.post('/api/comments?',
                    {
                             vote_id:id,
                             content:createdComment
                    },
                    {
                             headers:
                             {
                             'Content-Type': 'application/json'
                             }
                    })
                            .then((res) =>
                            {
                                axios.get('/api/comments?id=' + id + '&page=' + 1)
                                      .then((res) =>
                                      {
                                               // 댓글 데이터 설정
                                               setDataCount(res.data.comment.length)
                                               setcomments(res.data.comment)
                                               setVisibleData(res.data.comment)
                                               // 댓글 더보기 활성화 설정
                                               sethasNext(res.data.hasNext);
                                               setcommentPage(1);
                                               setcreatedComment('');
                                               setcommentCount(res.data.total)
                                      })
                            })
                            .catch((err) =>
                            {
                                message.error(err.response.data.result);
                                setcreatedComment('');
                            })
                 }
            }
            // 좋아요 함수
            const upClick = () =>
            {
                  axios.post('/api/ups?id=' + id)
                        .then((res) => {
                              message.success(res.data.result);
                              if(hasUp)
                              {
                                  sethasUp(false);
                                  setup(up - 1)

                              }
                              else
                              {
                                  sethasUp(true);
                                  setup(up + 1)
                              }
                        })
                        .catch((err) =>
                        {
                             message.error(err.response.data.result);
                        })
            }
            // 북마크 함수
            const bookmarkClick = () =>
            {
                     axios.post('/api/bookmarks?id=' + id)
                        .then((res) =>
                        {
                              message.success(res.data.result);
                              if(hasBookmark)
                              {
                                 sethasBookmark(false);
                              }
                              else
                              {
                                 sethasBookmark(true);
                              }
                        })
                        .catch((err) =>
                        {
                              message.error(err.response.data.result);
                        })
            }

            return (
                 <div>
                      <Flex gap="small" wrap justify="center" align="center" style={{ marginTop: '20px' }}>
                          <Space direction="vertical" size={16}>
                           {/* 제목 */}
                            <Card
                              title={voteNormaldata.title} // 데이터 기반 제목 출력
                              extra={
                                 vote_userId == userid ? (
                                      <Dropdown overlay={VoteMenu} trigger={['click']}>
                                           <Button type="primary">
                                             <MenuOutlined />
                                           </Button>
                                      </Dropdown>
                                 ) : ''
                              }
                              style={{ width: 500}}
                            >
                              {/* 선택지 데이터 순차 로딩 */}
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
                                                   {/* 선택지 내용 */}
                                                   <Button
                                                      type={selectedOption === option.id ? 'primary' : 'default'}
                                                      onClick={() => handleSelect(option.id)}
                                                      style={{ width: '100%' }}
                                                   >
                                                      {option.label}
                                                   </Button>
                                                   {/* 선택지 비율 */}
                                                   <Progress
                                                      percent={option.percent}
                                                      status="active"
                                                      format={(percent) => `${percent}%`} // 선택지와 퍼센트 표시
                                                   />
                                               </Card>
                              ))}

                              <Flex justify="end" align="center">
                                    <div style={{marginTop:10}}>
                                           {/* 좋아요 */}
                                           { !hasUp ? (
                                               <Button onClick={upClick}>
                                                   <LikeOutlined />
                                                   <span> {up} </span>
                                               </Button>
                                               ) : (
                                               <Button onClick={upClick}>
                                                    <LikeFilled />
                                                    <span> {up} </span>
                                               </Button>)
                                           }
                                           {/* 댓글 */}
                                           <Button>
                                               <CommentOutlined style={{}}/>
                                               <span> {commentCount} </span>
                                           </Button>
                                           {/* 북마크 */}
                                           { !hasBookmark ? (
                                               <Button onClick={bookmarkClick}>
                                                   <BookOutlined style={{fontSize:20}}/>
                                               </Button>
                                               ) : (
                                                <Button onClick={bookmarkClick}>
                                                   <BookFilled style={{fontSize:20}}/>
                                                </Button>
                                               )
                                           }
                                    </div>
                              </Flex>
                            </Card>
                             {/* 댓글 입력창 */}
                             <Space.Compact style={{width: '100%',height:100}}>
                                   <Input placeholder="댓글을 작성해보세요" count={{show:true,max:380}} onBlur={handleChange}/>
                                   <Button style={{height:100}}type="primary" onClick={commentSubmit}>작성</Button>
                             </Space.Compact>
                             { visibleData.length != 0 ? (
                               <Card style={{ width: 500}}>
                                    {/* 댓글 데이터 순차 로딩 */}
                                    {visibleData.map((comment) => (
                                          <div style={{border:'1px solid',borderRadius:'10px',marginTop:'10px',background:'#f8fafc',padding:'10px'}}>
                                              <Flex justify="space-between">
                                                  <div>
                                                       {/* 댓글 작성자 닉네임 */}
                                                       <span>{comment.user.user_nick}</span>
                                                       {/* 댓글 작성자 아이디 */}
                                                       <span>({comment.user.user_id})</span>
                                                       {/* 댓글 작성날짜 */}
                                                       <span style={{marginLeft:10,fontSize:11}}>* {comment.created_date}</span>
                                                  </div>
                                                  {/* 유저가 작성한 댓글이면 */}
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
                                              {/* 수정중인 댓글이면 */}
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
                                                  ) : ( <p style={{color:'#aaa'}}>{comment.content}</p> )
                                              }
                                          </div>
                                    ))}
                                    {/* 다음 데이터 존재하면 */}
                                    {hasNext && (
                                          <div className="button-container"> {/* 가운데 정렬을 위한 버튼 컨테이너 */}
                                                <Button onClick={handleLoadMore}>
                                                    더보기
                                                </Button>
                                           </div>
                                    )}
                                </Card>
                               ) : ''
                            }
                          </Space>
                      </Flex>
                 </div>
            );
}

export default VoteDetail;