// Layout.js
import React from 'react';
import { Layout as AntLayout } from 'antd';
import Header from './Header';
import Content from './Content';
import Footer from './Footer';

function Layout() {
  return (
  <div style={{ backgroundColor: '#f5f5f5', minHeight: '100vh' }}>
    <AntLayout>
          <Header />
          <AntLayout.Content>
          <Content />
          </AntLayout.Content>
          <Footer />
    </AntLayout>
   </div>
  );
}

export default Layout;