import {useEffect, useState} from "react";
import axios from "axios";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';  // 스타일 파일을 임포트
import Home from './Home';
import Layout from './Layout'
import VoteRate from './VoteRate'
import HotVote from './HotVote'
import VoteList from './VoteList'
import VoteDetail from './VoteDetail'
import VoteWrite from './VoteWrite'
import VoteUpdate from './VoteUpdate'
import Mypage from './Mypage'
import UserJoin from './UserJoin'
import Login from './Login'
function App() {
    return (
        <Router>
          <Routes>
            {/* 공통 레이아웃을 적용 */}
            <Route path="/" element={<Layout />}>
              {/* Home, About, Contact는 공통 레이아웃 안에서 렌더링 */}
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
          </Routes>
        </Router>
      );
}

export default App;