
import React, { useState } from "react";
import { Flex, Input, Typography,DatePicker, Space,Form, Button, Row, Col } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { EditOutlined } from '@ant-design/icons';
import { useParams } from 'react-router-dom';
const { RangePicker } = DatePicker;

function VoteUpdate() {
       const { id } = useParams();
       const [choices, setChoices] = useState([ { value: "" }, { value: "" } ]);

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
      };
      return (
        <Flex align="center" style={{height:'1000px'}}>
            <div style={{margin:'0 auto' , width:450,border:'1px solid',borderRadius:10,padding:30}}>
                <div>
                      <Typography.Title level={5}>제목</Typography.Title>
                      <Input
                        count={{
                          show: true,
                          max: 10,
                        }}
                        style={{width:'100%'}}
                        placeholder="제목을 입력해주세요"
                      />
                </div>
                <div>
                    <Typography.Title level={5}>날짜</Typography.Title>
                    <RangePicker
                          id={{
                            start: 'startInput',
                            end: 'endInput',
                          }}
                          onChange={(data,dataString) => {
                              console.log(dataString);
                          }}
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
                                    />
                                  </Form.Item>
                                </Col>
                                <Col span={4}>
                                  {choices.length > 1 && (
                                    <MinusCircleOutlined
                                      onClick={() => removeChoice(index)}
                                      style={{ fontSize: "20px", color: "red" }}
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
                     <Button style={{width:'100%'}}type="primary" shape="round" icon={<EditOutlined />} size={20}>
                                                       투표 수정
                                           </Button>
                </div>
            </div>
        </Flex>
      )
}


export default VoteUpdate;