// Header.js
import React, { useState } from 'react';
import { Menu, Button,Flex } from 'antd';
import { Link } from 'react-router-dom';
import { notification } from 'antd';
import './Header.css';  // 스타일 파일
import { useAuth } from './AuthContext';
import { UserOutlined } from '@ant-design/icons';
function Header() {
  const { isLoggedIn, logout,login  } = useAuth();
  const handleLogin = () => {
    login();
    notification.success({
      message: '로그인 성공',
    });
  };

  const handleLogout = () => {
    logout();
    notification.info({
      message: '로그아웃 성공',
    });
  };

  return (
    <header className="header">
      <div className="header-content">
         <Menu theme="dark" mode="horizontal" style={{  }}>
                  <Menu.Item key="1">
                    <Link to="/">Home</Link>
                  </Menu.Item>

                </Menu>
        <div className="auth-buttons">
          {isLoggedIn ? (
           <Flex justify="space-between" style={{width:160}}>
            <Link to="/mypage">
                <Button type="primary">
                   <UserOutlined style={{fontSize:25,marginBottom:'10px'}}/>
                 </Button>
            </Link>
            <Button onClick={handleLogout} type="primary">
              로그아웃
            </Button>
           </Flex>
          ) : (
           <Flex justify="space-between" style={{width:170}}>
             <Link to="/login">
                <Button type="primary">
                  로그인
                </Button>
             </Link>
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
