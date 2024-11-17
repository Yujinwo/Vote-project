import {useEffect, useState} from "react";
import axios from "axios";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';  // 스타일 파일을 임포트
import Home from './Home/Home';
import Layout from './Layout/Layout'
import VoteRate from './Vote/VoteRate'
import HotVote from './Vote/HotVote'
import VoteList from './Vote/VoteList'
import VoteDetail from './Vote/VoteDetail'
import VoteWrite from './Vote/VoteWrite'
import VoteUpdate from './Vote/VoteUpdate'
import Mypage from './MyPage/Mypage'
import UserJoin from './User/UserJoin'
import Login from './User/Login'
import {Result } from 'antd';
import { AuthProvider } from './Auth/AuthContext';
const NotFoundPage = () => (
    <Result
      status="404"
      title="404"
      subTitle="방문하신 페이지는 존재하지 않습니다"
    />
);
function App() {
    return (
       <AuthProvider>
        <Router>
          <Routes>
            {/* 공통 레이아웃을 적용 */}
            <Route path="/" element={<Layout />}>
              <Route index element={<Home />} />
              <Route path="voterate" element={<VoteRate />} />
              <Route path="hotvote" element={<HotVote />} />
              <Route path="votelist" element={<VoteList />} />
              <Route path="votedetail/:id" element={<VoteDetail />} />
              <Route path="votewrite" element={<VoteWrite />} />
              <Route path="voteupdate/:id" element={<VoteUpdate />} />
              <Route path="mypage" element={<Mypage />} />
              <Route path="userjoin" element={<UserJoin />} />
              <Route path="login" element={<Login />} />
            </Route>
             <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Router>
      </AuthProvider>
      );
}

export default App;