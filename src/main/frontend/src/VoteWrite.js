
import React, { useState } from "react";
import { Flex, Input, Typography,DatePicker, Space,Form, Button, Row, Col,message,Select } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { EditOutlined } from '@ant-design/icons';
const { RangePicker } = DatePicker;

function VoteWrite() {
       const [title,setTitle] = useState('');
       const [day,setDay] = useState('');
       const [choices, setChoices] = useState([ { value: "" }, { value: "" } ]);
       const [titleisExceeded, settitleIsExceeded] = useState(true);
       const [choiceOneisExceeded, setchoiceOneIsExceeded] = useState(true);
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
      // 선택지 추가 함수
      const addChoice = () => {
        if (choices.length < 2) {
          setChoices([...choices, { value: "" }]);
        }
      };

      // 선택지 삭제 함수
      const removeChoice = (index) => {
        const newChoices = choices.filter((_, i) => i !== index);
        setChoices(newChoices);
      };

      // 선택지 값 변경 함수
      const handleChange = (index, event) => {
        const newChoices = [...choices];
        newChoices[index].value = event.target.value;
        setChoices(newChoices);
        const maxLength = 10;
        const value = event.target.value;
        if(index == 1)
        {
         setchoiceOneIsExceeded(value.length >= maxLength || ( value === null && value.length <= maxLength) || value.trim() === '');
        }
        else {
         setchoiceTwoIsExceeded(value.length >= maxLength || ( value === null && value.length <= maxLength) || value.trim() === '');
        }

      };
      // 투표 작성 함수
      const writeVote = () => {
          if (titleisExceeded || choiceOneisExceeded || choiceTwoisExceeded || day.length != 2) {
            message.error("입력을 확인해주세요");
          } else {
            message.success('작성 성공!');
          }
         console.log(title);
         console.log(day);
         console.log(choices);
      }
      // 제목 변경 함수
      const changeTitle = (e) => {
         setTitle(e.target.value)
         const maxLength = 10;
         const value = e.target.value;
         settitleIsExceeded(value.length >= maxLength || ( value === null && value.length <= maxLength) || value.trim() === '');
      }
      const changeDay =(data,dataString) => {
         setDay(dataString);
      }

      const handleChangecategory = (value) => {
              setCategoryValue(value); // 선택된 값 저장
              console.log('Selected category:', value); // 선택된 값 콘솔에 출력
      };
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
                          max: 10,
                        }}
                        style={{width:'100%'}}
                        placeholder="제목을 입력해주세요"
                        onBlur={changeTitle}
                      />
                </div>
                <div>
                    <Typography.Title level={5}>날짜</Typography.Title>
                    <RangePicker
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
                  <Form style={{height:150}}>
                      <Typography.Title level={5}>선택 의견</Typography.Title>
                       {choices.map((choice, index) => (
                              <Row key={index} gutter={16} >
                                <Col span={20}>
                                  <Form.Item>
                                    <Input
                                      placeholder={`Choice ${index + 1}`}
                                      value={choice.value}
                                      onChange={(event) => handleChange(index, event)}
                                      style={{width:'100%'}}
                                      count={{
                                              show: true,
                                              max: 10,
                                      }}
                                    />
                                  </Form.Item>
                                </Col>
                                <Col span={4}>
                                  {choices.length > 1 && (
                                    <MinusCircleOutlined
                                      onClick={() => removeChoice(index)}
                                      style={{ fontSize: "20px", color: "red",marginTop:5 }}
                                    />
                                  )}
                                </Col>
                              </Row>
                            ))}

                      <Form.Item>
                        {choices.length < 2 && (
                          <Button
                            type="dashed"
                            onClick={addChoice}
                            icon={<PlusOutlined />}
                          >
                            Add Choice
                          </Button>
                        )}
                      </Form.Item>


                    </Form>
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