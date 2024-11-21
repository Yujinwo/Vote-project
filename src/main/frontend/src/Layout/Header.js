import React, { useState } from 'react';
import { Menu, Button,Flex } from 'antd';
import { Link } from 'react-router-dom';
import { notification } from 'antd';
import '../css/Header.css';  // 스타일 파일
import { useAuth } from '../Auth/AuthContext';
import { UserOutlined } from '@ant-design/icons';
import axios from  '../apiClient'
function Header() {
  const { isLoggedIn, logout,login  } = useAuth();
  const handleLogout = () =>
  {
    logout();
    axios.post('/api/logout')
           .then((res) =>
           {
                 sessionStorage.removeItem('accessToken');
                 notification.info({
                      message: res.data.result,
                 });
           })
           .catch((err) =>
           {
                 notification.info({
                      message: err.response.data.result,
                 });
           })
  };

  return (
    <header className="header">
      <div className="header-content">
         {/* 홈 버튼 */}
         <Menu theme="dark" mode="horizontal">
           <Menu.Item key="1">
                 <Link to="/">Home</Link>
           </Menu.Item>
         </Menu>
        <div className="auth-buttons">
          {/* 총 투표 수 */}
          {isLoggedIn ? (
           <Flex justify="space-between" style={{width:160}}>
              {/* My페이지 버튼 */}
              <Link to="/mypage">
                  <Button type="primary">
                     <UserOutlined style={{fontSize:25,marginBottom:'10px'}}/>
                  </Button>
              </Link>
              {/* 로그아웃 버튼 */}
              <Button onClick={handleLogout} type="primary">
                  로그아웃
              </Button>
           </Flex>
           ) : (
           <Flex justify="space-between" style={{width:170}}>
             {/* 로그인 버튼 */}
             <Link to="/login">
                <Button type="primary">
                  로그인
                </Button>
             </Link>
             {/* 회원가입 버튼 */}
             <Link to="/userjoin">
                <Button type="primary">
                  회원가입
                </Button>
             </Link>
           </Flex>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;
