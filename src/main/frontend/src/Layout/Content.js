import React from 'react';
import { Outlet } from 'react-router-dom';

function Content() {
  return (
    <main style={{ padding: '20px', minHeight: '80vh' }}>
      <Outlet />
    </main>
  );
}

export default Content;