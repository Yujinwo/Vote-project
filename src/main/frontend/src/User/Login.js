import React,{useState} from 'react'
import { Flex, Input, Typography,Button, message } from 'antd';
import { CheckOutlined,ExclamationCircleOutlined } from '@ant-design/icons';
import { useAuth } from '../Auth/AuthContext';
import axios from "axios";
import { useNavigate } from 'react-router-dom';
const { Text, Link } = Typography;
function Login() {
        const {isLoggedIn,login } = useAuth();
        const navigate = useNavigate();
        const [idValue, setIdValue] = useState('');
        const [pwValue, setPwValue] = useState('');
        const [idisExceeded, setidIsExceeded] = useState(true);
        const [pwisExceeded, setpwIsExceeded] = useState(true);
        const [idError, setidError] = useState('');
        const [pwError, setpwError] = useState('');
        const idChange = (e) => {
        const minLength = 4;
        const maxLength = 10;
        const value = e.target.value.trim();
        if( (value.length < minLength || value.length > maxLength ) && (/^[a-zA-Z0-9]*$/.test(value) && !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ) )
        {
           setidIsExceeded(true)
           setidError("최소 4자 이상, 최대 10자 이하로 입력해주세요")
        }
        else if (value === null || value.trim() === '')
        {
           setidIsExceeded(true);
           setidError("선택지를 입력해주세요");
        }
        else if(!(/^[a-zA-Z0-9]*$/.test(value) && !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value)))
        {
           setidIsExceeded(true)
           setidError("알파벳, 숫자 조합으로 입력해주세요")
        }
        else
        {
           setIdValue(value);
           setidIsExceeded(false)
           setidError('')
        }
        };
        const pwChange = (e) =>
        {
             const minLength = 7;
             const maxLength = 15;
             const value = e.target.value.trim();
             setPwValue(value);
             if(value.length < minLength || value.length > maxLength)
             {
                   setpwIsExceeded(true)
                   setpwError("최소 7자 이상, 최대 15자 이하로 입력해주세요")
             }
             else if (value === null || value.trim() === '')
             {
                   setpwIsExceeded(true);
                   setpwError("선택지를 입력해주세요");
             }
             else {
                   setpwIsExceeded(false)
                   setpwError('')
             }
        };
        const Login = () =>
        {
            if (idisExceeded || pwisExceeded )
            {
                message.error("입력과 규칙을 확인해주세요");
            }
            else
            {
                axios.post('/api/login',
                {
                      user_id: idValue,
                      user_pw: pwValue
                },
                {
                      headers:
                      {
                          'Content-Type': 'application/json'
                      }
                })
                .then((res) =>
                {
                      login(idValue);
                      navigate('/');
                      message.success('로그인 성공!');
                })
                .catch((err) =>
                {
                      message.error(err.response.data.result);
                });
            }
        };

        return (
         <Flex  style={{height:1000}} align="center">
              <div style={{width:500,margin:'0 auto',border:'1px solid',borderRadius:10,padding:20}}>
                {/* 아이디 입력창 */}
                <div>
                      <Typography.Title level={5}>아이디</Typography.Title>
                      <Input
                        count={{
                          show: true,
                          max: 10,
                        }}
                        placeholder="아이디를 입력해주세요"
                        onBlur={idChange}
                      />
                </div>
                {/* 아이디 오류 알림 */}
                {  idError != '' ? (
                    <Flex align="center">
                        <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                        <Text type="danger">{idError}</Text>
                    </Flex>
                   ) : ''
                }
                {/* 비밀번호 입력창 */}
                <div>
                      <Typography.Title level={5}>비밀번호 </Typography.Title>
                      <Input.Password
                        count={{
                          show: true,
                          max: 10,
                        }}
                        placeholder="비밀번호를 입력해주세요"
                        onBlur={pwChange}
                      />
                </div>
                {/* 비밀번호 오류 알림 */}
                {  pwError != '' ? (
                    <Flex align="center">
                        <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                        <Text type="danger">{pwError}</Text>
                    </Flex>
                ) : ''
                }
                {/* 로그인 버튼 */}
                <div style={{marginTop:30}}>
                         <Button onClick={Login} style={{width:'100%'}}type="primary" shape="round" icon={<CheckOutlined />} size={40}>
                              로그인
                         </Button>
                </div>
              </div>
         </Flex>
        );
}

export default Login;