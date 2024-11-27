import React, { useState } from 'react';
import { Flex, Input, Typography,Button, message } from 'antd';
import { UserAddOutlined,ExclamationCircleOutlined } from '@ant-design/icons';
import { useAuth } from '../Auth/AuthContext';
import axios from  "../apiClient"
import { useNavigate } from 'react-router-dom';
const { Text, Link } = Typography;
function UserJoin() {
  const { isLoggedIn, login  } = useAuth();
  const [idValue, setIdValue] = useState('');
  const [pwValue, setPwValue] = useState('');
  const [confirmpwValue, setconfirmPwValue] = useState('');
  const [nickValue, setNickValue] = useState('');
  const [idisExceeded, setidIsExceeded] = useState(true);
  const [pwisExceeded, setpwIsExceeded] = useState(true);
  const [confirmpwisExceeded, setConfirmppwIsExceeded] = useState(true);
  const [nickisExceeded, setnickIsExceeded] = useState(true);
  const [idError, setidError] = useState('');
  const [pwError, setpwError] = useState('');
  const [confirmpwError, setconfirmpwError] = useState('');
  const [nickError, setnickError] = useState('');
  const navigate = useNavigate();

  const idChange = (e) => {
    const minLength = 4;
    const maxLength = 10;
    const value = e.target.value.trim();
    if( (value.length < minLength || value.length > maxLength ) && (/^[a-zA-Z0-9]*$/.test(value) && !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value)) )
    {
       setidIsExceeded(true)
       setidError("최소 4자 이상, 최대 10자 이하로 입력해주세요")
    }
    else if (value === null || value.trim() === '')
    {
       setidIsExceeded(true);
       setidError("아이디를 입력해주세요");
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
       if(value != null && value != '')
       {
             axios.get('/api/users?user_id=' +  value)
                     .then((res) =>
                     {
                            setidIsExceeded(false)
                            message.info("사용할 수 있는 아이디 입니다")
                     })
                     .catch((err) =>
                     {
                            if(err.response.data.errorMsg != null) {
                              message.info(err.response.data.errorMsg)
                            }
                            else {
                              message.info(err.response.data.result)
                            }
                            setidIsExceeded(true)

                     })
           }
    }

  };

  const pwChange = (e) => {
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
            setpwError("비밀번호를 입력해주세요");
     }
     else
     {
            setpwIsExceeded(false)
            setpwError('')
     }
  };
  const confirmPwChange = (e) => {
     const minLength = 7;
     const maxLength = 15;
     const value = e.target.value.trim();
     setconfirmPwValue(value);
     if(value.length < minLength || value.length > maxLength)
     {
            setConfirmppwIsExceeded(true)
            setconfirmpwError("최소 7자 이상, 최대 15자 이하로 입력해주세요")
     }
     else if (value === null || value.trim() === '')
     {
            setConfirmppwIsExceeded(true);
            setconfirmpwError("재확인 비밀번호를 입력해주세요");
     }
     else if(pwValue != value)
     {
            setConfirmppwIsExceeded(true)
            setconfirmpwError("비밀번호가 일치하지 않습니다")
     }
     else
     {
            setConfirmppwIsExceeded(false)
            setconfirmpwError('')
     }
  };
  const nickChange = (e) => {
      const minLength = 2;
      const maxLength = 6;
      const value = e.target.value.trim();
      if( ( value.length < minLength || value.length > maxLength ) &&  ( !/^[ㄱ-ㅎ]*$/.test(value) && !/[ㄱ-ㅎ]/.test(value) ) )
      {
            setnickIsExceeded(true);
            setnickError("최소 2자 이상, 최대 6자 이하로 입력해주세요")
      }
      else if (value === null || value.trim() === '')
      {
            setnickIsExceeded(true);
            setnickError("닉네임을 입력해주세요");
      }
      // 한글 초성 불가능 규칙 확인
      else if (/^[ㄱ-ㅎ]*$/.test(value) || /[ㄱ-ㅎ]/.test(value))
      {
            setnickIsExceeded(true);
            setnickError("한글 초성은 불가능합니다");
      }
      else
      {
            setnickIsExceeded(false);
            setNickValue(value);
            setnickError('');
      }

};

  const SignUp = () => {
    if (idisExceeded || pwisExceeded || confirmpwisExceeded || nickisExceeded)
    {
          message.error("입력과 규칙을 확인해주세요");
    }
    else
    {
          axios.post('/api/users',
          {
                  user_id: idValue,
                  user_pw: pwValue,
                  user_nick: nickValue,
                  user_confirmpw: confirmpwValue
          },
          {
                  headers: {
                     'Content-Type': 'application/json'
                  }
          })
          .then((res) =>
          {
                  login(idValue);
                  navigate('/');
                  message.success('회원가입 성공');
          })
          .catch((err) =>
          {
                  if(err.response.data.errorMsg != null) {
                     message.error(err.response.data.errorMsg)
                  }
                  else {
                      message.error(err.response.data.result)
                  }
          })
    }
  };
    return (
      <Flex style={{height:1000}} align="center">
        <div style={{width:500,margin:'0 auto',border:'1px solid',borderRadius:10,padding:30}}>
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
                  <Typography.Title level={5}>비밀번호</Typography.Title>
                  <Input.Password
                    count={{
                      show: true,
                      max: 15,
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
            {/* 재확인 비밀번호 입력창 */}
            <div>
                  <Typography.Title level={5}>재확인 비밀번호 </Typography.Title>
                  <Input.Password
                    count={{
                      show: true,
                      max: 10,
                    }}
                    placeholder="재확인 비밀번호를 입력해주세요"
                    onBlur={confirmPwChange}
                  />
            </div>
            {/* 재확인 비밀번호 오류 알림 */}
            {  confirmpwError != '' ? (
                    <Flex align="center">
                        <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                        <Text type="danger">{confirmpwError}</Text>
                    </Flex>
                ) : ''
            }
            {/* 닉네임 입력창 */}
            <div>
                  <Typography.Title level={5}>닉네임 </Typography.Title>
                  <Input
                    count={{
                      show: true,
                      max: 6,
                    }}
                    placeholder="닉네임을 입력해주세요"
                    onBlur={nickChange}
                  />
            </div>
            {/* 닉네임 오류 알림 */}
            {  nickError != '' ? (
                    <Flex align="center">
                        <ExclamationCircleOutlined style={{marginBottom:10,marginRight:10}}/>
                        <Text type="danger">{nickError}</Text>
                    </Flex>
                ) : ''
            }
            {/* 회원가입 버튼 */}
            <div style={{marginTop:30}}>
               <Button onClick={SignUp} style={{width:'100%'}}type="primary" shape="round" icon={<UserAddOutlined />} size={40}>
                   회원 가입
               </Button>
            </div>
        </div>
      </Flex>


    );

}

export default UserJoin;