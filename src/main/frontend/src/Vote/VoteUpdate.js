
import React, { useState,useEffect } from "react";
import { Flex, Input, Typography,DatePicker, Space,Form, Button, Row, Col,message,Select } from 'antd';
import { DeleteOutlined, PlusOutlined ,ExclamationCircleOutlined} from "@ant-design/icons";
import { EditOutlined } from '@ant-design/icons';
import { useNavigate,useParams } from 'react-router-dom';
import axios from  "../apiClient"
import { useAuth } from '../Auth/AuthContext';
import dayjs from 'dayjs';
const { RangePicker } = DatePicker;
const { Text, Link } = Typography;

function VoteUpdate() {
       const { id } = useParams();
       const { isLoggedIn, login ,userid } = useAuth();
       const navigate = useNavigate();
       const [title,setTitle] = useState('');
       const [titleError, settitleError] = useState('기존 제목을 수정해주세요');
       const [day, setDay] = useState([dayjs(), dayjs()]);
       const [choices, setChoices] = useState(["",""]);
       const [titleisExceeded, settitleIsExceeded] = useState(true);
       const [choiceOneisExceeded, setchoiceOneIsExceeded] = useState(true);
       const [choiceError, setchoiceError] = useState('기존 내용을 수정해주세요');
       const [choiceTwoisExceeded, setchoiceTwoIsExceeded] = useState(true);
       const [CategoryValue, setCategoryValue] = useState(null);

       // 기본값 설정
       const [initialTitle,setinitialTitle] = useState('');
       const [initialCategory,setinitialCategory] = useState(null);
       const [initialChoices ,setinitialChoices] = useState(null);


       const categorys =
       [
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

     useEffect(() =>
     {
              // 투표 데이터 조회
              axios.get('/api/votes?id=' + id)
                .then((res) => {
                       if(res.data.vote.user.user_id == null) {
                                    navigate('/votelist');
                                    message.error('로그인을 해주세요');
                       }
                       else if (res.data.vote.user.user_id != userid) {
                                    navigate('/votelist');
                                    message.error('허용되지 않은 접근입니다');
                       }
                       setCategoryValue(res.data.vote.category);
                       setinitialCategory(res.data.vote.category);

                       setTitle(res.data.vote.title);
                       setinitialTitle(res.data.vote.title);

                       setChoices([res.data.vote.voteOptions[0].content,res.data.vote.voteOptions[1].content]);
                       setinitialChoices([res.data.vote.voteOptions[0].content,res.data.vote.voteOptions[1].content]);

                       dayset(res.data.vote.startDay,res.data.vote.endDay);
                })
     },[])
     // 시간 데이터 형식 변환 함수
     const dayset = (startDay,endDay) =>
     {

         const date = new Date(startDay);
         const date2 = new Date(endDay);

         const startDateStr = date.getFullYear() + "-" +
                              String(date.getMonth() + 1).padStart(2, '0') + "-" +
                              String(date.getDate()).padStart(2, '0') + " " +
                              String(date.getHours()).padStart(2, '0') + ":" +
                              String(date.getMinutes()).padStart(2, '0') + ":" +
                              String(date.getSeconds()).padStart(2, '0');

         const EndDateStr = date2.getFullYear() + "-" +
                            String(date2.getMonth() + 1).padStart(2, '0') + "-" +
                            String(date2.getDate()).padStart(2, '0') + " " +
                            String(date2.getHours()).padStart(2, '0') + ":" +
                            String(date2.getMinutes()).padStart(2, '0') + ":" +
                            String(date2.getSeconds()).padStart(2, '0');

         setDay([startDateStr,EndDateStr])
     }
     // 선택지 내용 지우기 함수
     const removeChoice = (index) =>
     {
         const newChoices = [...choices];
         console.log(newChoices);
         newChoices[index]= '';
         setChoices(newChoices);
     };

     // 선택지 값 변경 함수
     const handleChange = (index, event) =>
     {
         const newChoices = [...choices];
         console.log(newChoices);
         newChoices[index] = event.target.value;

         setChoices(newChoices);
         const minLength = 5;
         const maxLength = 20;
         const value = event.target.value;
         if(index == 0)
         {
              if( ( value.length < minLength || value.length > maxLength ) &&  ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ) )
              {
                     setchoiceOneIsExceeded(true);
                     setchoiceError("최소 5자 이상, 최대 20자 이하로 입력해주세요")
              }
              else if (value == initialChoices[0])
              {
                     settitleIsExceeded(true);
                     setchoiceError("기존 내용과 동일합니다");
              }
              else if (value === null || value.trim() === '')
              {
                     setchoiceOneIsExceeded(true);
                     setchoiceError("선택지를 입력해주세요");
              }
              // 한글 초성 불가능 규칙 확인
              else if ( /^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value) )
              {
                     setchoiceOneIsExceeded(true);
                     setchoiceError("한글 초성은 불가능합니다");
              }
              else
              {
                     setchoiceOneIsExceeded(false);
                     setchoiceError('');
              }
         }
         else
         {
              if( ( value.length < minLength || value.length > maxLength ) &&  ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ))
              {
                     setchoiceTwoIsExceeded(true);
                     setchoiceError("최소 5자 이상, 최대 20자 이하로 입력해주세요")
              }
             else if (value == initialChoices[1])
             {
                     settitleIsExceeded(true);
                     setchoiceError("기존 내용과 동일합니다");
             }
             else if (value === null || value.trim() === '')
             {
                     setchoiceTwoIsExceeded(true);
                     setchoiceError("선택지를 입력해주세요");
             }
             // 한글 초성 불가능 규칙 확인
             else if ( /^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value) )
             {
                     setchoiceTwoIsExceeded(true);
                     setchoiceError("한글 초성은 불가능합니다");
             }
             else
             {
                     setchoiceTwoIsExceeded(false);
                     setchoiceError('');
             }
         }
      }

      // 투표 작성 함수
      const writeVote = () =>
      {
          if (titleisExceeded || choiceOneisExceeded || choiceTwoisExceeded || day.length != 2 || choices.length != 2)
          {
              message.error("입력을 확인해주세요");
          }
          else
          {
             axios.put('/api/votes',
            {
                    vote_id:id,
                    title:title,
                    category:CategoryValue,
                    days : day,
                    choices : choices
             },
             {
                    headers:
                    {
                       'Content-Type': 'application/json'
                    }
             })
             .then((res) =>
             {
                    navigate('/votedetail/' + id);
                    message.success('투표 수정 완료');
             })
             .catch((err) =>
             {
                    message.error(err.response.data.result);
             })
          }
      }

      // 제목 변경 함수
      const changeTitle = (e) =>
      {
         const minLength = 5;
         const maxLength = 20;
         const value = e.target.value;
         setTitle(value);
         if( (value.length < minLength || value.length > maxLength ) && ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ))
         {
               settitleIsExceeded(true);
               settitleError("최소 5자 이상, 최대 20자 이하로 입력해주세요");
         }
         else if (value == initialTitle)
         {
               settitleIsExceeded(true);
               settitleError("기존 제목과 동일합니다");
         }
         else if (value === null || value.trim() === '')
         {
               settitleIsExceeded(true);
               settitleError("제목을 입력해주세요");
         }
         else if(/^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value))
         {
               settitleIsExceeded(true);
               settitleError("한글 초성은 불가능합니다");
         }
         else
         {
               settitleIsExceeded(false);
               settitleError('');
         }
      }
      
      // 기간 설정 함수
      const changeDay =(data,dataString) =>
      {
         setDay(dataString);
      }

      // 카테고리 설정 함수
      const handleChangecategory = (value) =>
      {
            if(initialCategory == value)
            {
                message.error('기존 카테고리와 동일합니다')
            }
            else {
                setCategoryValue(value);
            }
      }

      return (
        <Flex align="center" style={{height:'1000px'}}>
            <div style={{margin:'0 auto' , width:450,border:'1px solid',borderRadius:10,padding:30}}>
                <Typography.Title level={5}>카테고리</Typography.Title>
                {/* 카테고리 선택 버튼 */}
                <Select
                      showSearch
                      placeholder="카테고리 선택"
                      onChange={handleChangecategory}
                      filterOption={(input, option) =>
                             (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                      }
                      style={{width:'100%'}}
                      options={categorys}
                      value={CategoryValue}
                />
                {/* 제목 입력창 */}
                <div>
                      <Typography.Title level={5}>제목</Typography.Title>
                      <Input
                        count={{
                          show: true,
                          max: 20,
                        }}
                        style={{width:'100%'}}
                        placeholder="제목을 입력해주세요"
                        onChange={changeTitle}
                        value={title}
                      />
                </div>
                {/* 제목 오류 알림 */}
                {  titleError != '' ?
                   (
                       <Flex align="center">
                           <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                           <Text type="danger">{titleError}</Text>
                       </Flex>
                   ) : ''
                }
                {/* 기간 설정 버튼 */}
                <div>
                    <Typography.Title level={5}>날짜</Typography.Title>
                    <RangePicker
                          showTime
                          id={{
                            start: 'startInput',
                            end: 'endInput',
                          }}
                          size="middle"
                          onChange={changeDay}
                          style={{width:'100%'}}
                        value={day[0] && day[1] ? [dayjs(day[0]), dayjs(day[1])] : [null, null]} // 두 날짜 모두 설정
                    />
                </div>

                <div>
                  {/* 선택지 설정 버튼 */}
                  <Form style={{height:130}}>
                      <Typography.Title level={5}>선택 의견</Typography.Title>
                       {choices.map((choice, index) => (
                              <Row key={index} gutter={16} >
                                <Col span={20}>
                                  <Form.Item>
                                    <Input
                                      placeholder={`Choice ${index + 1}`}
                                      value={choice}
                                      onChange={(event) => handleChange(index, event)}
                                      style={{width:'100%'}}
                                      count={{
                                              show: true,
                                              max: 20,
                                      }}
                                    />
                                  </Form.Item>
                                </Col>
                                <Col span={4}>
                                  {choices.length > 1 && (
                                    <DeleteOutlined
                                      onClick={() => removeChoice(index)}
                                      style={{ fontSize: "20px", color: "red",marginTop:5 }}
                                    />
                                  )}
                                </Col>
                              </Row>
                            ))}
                  </Form>
                  {/* 선택지 오류 알림 */}
                  {  choiceError != '' ?
                         (
                               <Flex align="center">
                                    <ExclamationCircleOutlined style={{marginBottom:4,marginRight:10}}/>
                                    <Text type="danger">{choiceError}</Text>
                               </Flex>
                         ) : ''
                  }
                  <Button onClick={writeVote} style={{width:'100%'}}type="primary" shape="round" icon={<EditOutlined />} size={20}>
                         투표 작성
                  </Button>
                </div>
            </div>
        </Flex>
      )
}


export default VoteUpdate;