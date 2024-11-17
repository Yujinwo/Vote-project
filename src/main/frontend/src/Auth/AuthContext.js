import React, { createContext, useState, useContext,useEffect } from 'react';
import { message } from 'antd';
import axios from 'axios'

// AuthContext 생성
const AuthContext = createContext();

// Context를 제공하는 컴포넌트
export const AuthProvider = ({ children }) => {
  //로그인 여부
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userid, setuserid] = useState('');

  //로그인
  const login = (userid) =>
  {
         setuserid(userid);
         setIsLoggedIn(true);
  }
  //로그아웃
  const logout = () =>
  {
         setIsLoggedIn(false);
  }

  // 컴포넌트가 마운트될 때 상태를 복원
  useEffect(() => {
       // 세션 조회
       axios.get('/api/sessions')
         .then((res) =>
         {
               setuserid(res.data.result);
               setIsLoggedIn(true);
         })
         .catch((err) =>
         {
               setIsLoggedIn(false);
         })
  }, []);

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout,userid }}>
      {children}
    </AuthContext.Provider>
  );
};

// AuthContext를 사용하기 위한 Hook
export const useAuth = () => useContext(AuthContext);