import React, { createContext, useState, useContext } from 'react';

// AuthContext 생성
const AuthContext = createContext();

// Context를 제공하는 컴포넌트
export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const login = () => setIsLoggedIn(true);
  const logout = () => setIsLoggedIn(false);

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// AuthContext를 사용하기 위한 Hook
export const useAuth = () => useContext(AuthContext);