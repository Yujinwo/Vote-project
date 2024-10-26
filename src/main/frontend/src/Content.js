// Content.js
import React from 'react';
import { Outlet } from 'react-router-dom';

function Content() {
  return (
    <main style={{ padding: '20px', minHeight: '80vh' }}>
      {/* Outlet을 사용하여 자식 컴포넌트가 여기서 렌더링 */}
      <Outlet />
    </main>
  );
}

export default Content;