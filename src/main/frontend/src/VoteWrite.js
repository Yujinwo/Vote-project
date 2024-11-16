
import React, { useState,useEffect } from "react";
import { Flex, Input, Typography,DatePicker, Space,Form, Button, Row, Col,message,Select } from 'antd';
import { DeleteOutlined, PlusOutlined ,ExclamationCircleOutlined} from "@ant-design/icons";
import { EditOutlined } from '@ant-design/icons';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
const { RangePicker } = DatePicker;
const { Text, Link } = Typography;
function VoteWrite() {
      const { isLoggedIn, login  } = useAuth();
      const navigate = useNavigate();
       useEffect(() => {
            axios.get('/api/sessions')
                 .then((res) => {
                 })
                 .catch((err) => {
                        navigate('/votelist');
                        message.error('로그인을 해주세요');
                 })
       },[])


       const [title,setTitle] = useState('');
       const [titleError, settitleError] = useState('');
       const [day,setDay] = useState('');
       const [choices, setChoices] = useState(["",""]);
       const [titleisExceeded, settitleIsExceeded] = useState(true);
       const [choiceOneisExceeded, setchoiceOneIsExceeded] = useState(true);
       const [choiceError, setchoiceError] = useState('');
       const [choiceTwoisExceeded, setchoiceTwoIsExceeded] = useState(true);
       const [CategoryValue, setCategoryValue] = useState(null);
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


      // 선택지 내용 지우기 함수
      const removeChoice = (index) => {
        const newChoices = [...choices];
        console.log(newChoices);
        newChoices[index]= '';
        setChoices(newChoices);
      };

      // 선택지 값 변경 함수
      const handleChange = (index, event) => {
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
               else if (value === null || value.trim() === '') {
                      setchoiceOneIsExceeded(true);
                      setchoiceError("선택지를 입력해주세요");
               }
               // 한글 초성 불가능 규칙 확인
               else if ( /^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value) ) {
                       setchoiceOneIsExceeded(true);
                       setchoiceError("한글 초성은 불가능합니다");
               }
               else {
                       setchoiceOneIsExceeded(false);
                       setchoiceError('');
               }
        }
        else {
             if( ( value.length < minLength || value.length > maxLength ) &&  ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ))
                   {
                       setchoiceTwoIsExceeded(true);
                       setchoiceError("최소 5자 이상, 최대 20자 이하로 입력해주세요")
                   }
                   else if (value === null || value.trim() === '') {
                       setchoiceTwoIsExceeded(true);
                       setchoiceError("선택지를 입력해주세요");
                   }
                   // 한글 초성 불가능 규칙 확인
                   else if ( /^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value) ) {
                       setchoiceTwoIsExceeded(true);
                       setchoiceError("한글 초성은 불가능합니다");
                   }
                   else {
                       setchoiceTwoIsExceeded(false);
                       setchoiceError('');
                   }
            }
      }
      // 투표 작성 함수
      const writeVote = () => {
          if (titleisExceeded || choiceOneisExceeded || choiceTwoisExceeded || day.length != 2 || choices.length != 2) {
            message.error("입력을 확인해주세요");
          } else {

            axios.post('/api/votes',{
                                  title:title,
                                  category:CategoryValue,
                                  days : day,
                                  choices : choices
                                },{
                                   headers: {
                                      'Content-Type': 'application/json'
                                   }
                                })
                                .then((res) => {
                                  navigate('/votelist');
                                  message.success('투표 작성 완료');
                                })
                                .catch((err) => {
                                  message.error(err.response.data.result);
                                })
          }


      }
      // 제목 변경 함수
      const changeTitle = (e) => {

         const minLength = 5;
         const maxLength = 20;
         const value = e.target.value;
         if( (value.length < minLength || value.length > maxLength ) && ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) )){
           settitleIsExceeded(true);
           settitleError("최소 5자 이상, 최대 20자 이하로 입력해주세요");
         }
         else if (value === null || value.trim() === '') {
           settitleIsExceeded(true);
           settitleError("제목을 입력해주세요");
         }
         else if(/^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value)) {
           settitleIsExceeded(true);
           settitleError("한글 초성은 불가능합니다");
         }
         else {
           settitleIsExceeded(false);
           setTitle(value);
           settitleError('');
         }
      }
      const changeDay =(data,dataString) => {
         setDay(dataString);
      }

      const handleChangecategory = (value) => {
              setCategoryValue(value); // 선택된 값 저장
      }

      return (
        <Flex align="center" style={{height:'1000px'}}>
            <div style={{margin:'0 auto' , width:450,border:'1px solid',borderRadius:10,padding:30}}>
                <Typography.Title level={5}>카테고리</Typography.Title>
                <Select
                      showSearch
                      placeholder="카테고리 선택"
                      onChange={handleChangecategory} // 선택된 값을 가져오는 핸들러
                      filterOption={(input, option) =>
                             (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                      }
                      style={{width:'100%'}}
                      options={categorys}
                />
                <div>
                      <Typography.Title level={5}>제목</Typography.Title>
                      <Input
                        count={{
                          show: true,
                          max: 20,
                        }}
                        style={{width:'100%'}}
                        placeholder="제목을 입력해주세요"
                        onBlur={changeTitle}
                      />
                </div>
                {  titleError != '' ? (
                        <Flex align="center">
                            <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                            <Text type="danger">{titleError}</Text>
                        </Flex>
                    ) : ''
                }
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
                        />
                </div>

                <div>
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
                          {  choiceError != '' ? (
                                                                <Flex align="center">
                                                                     <ExclamationCircleOutlined style={{marginBottom:4,marginRight:10}}/>
                                                                     <Text type="danger">{choiceError}</Text>
                                                                </Flex>
                                                    ) : ''
                          }
                     <Button onClick={writeVote}
                     style={{width:'100%'}}type="primary" shape="round" icon={<EditOutlined />} size={20}>
                                                       투표 작성
                     </Button>
                </div>
            </div>
        </Flex>
      )
}


export default VoteWrite;