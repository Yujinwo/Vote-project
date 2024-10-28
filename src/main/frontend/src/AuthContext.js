import React, { createContext, useState, useContext,useEffect } from 'react';
import { message } from 'antd';
import axios from 'axios'
// AuthContext 생성
const AuthContext = createContext();

// Context를 제공하는 컴포넌트
export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // 토큰 기반으로 수정
  const login = () => {
                 setIsLoggedIn(true);
  }
  const logout = () => {
                 setIsLoggedIn(false);

  }

  // 컴포넌트가 마운트될 때 상태를 복원
  useEffect(() => {
            axios.get('/api/sessions')
                   .then((res) => {
                       setIsLoggedIn(true);
                   })
                   .catch((err) => {
                       setIsLoggedIn(false);
                   })
  }, []);


  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// AuthContext를 사용하기 위한 Hook
export const useAuth = () => useContext(AuthContext);