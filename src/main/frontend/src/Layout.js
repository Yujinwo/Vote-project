// Layout.js
import React from 'react';
import { Layout as AntLayout } from 'antd';
import Header from './Header';
import Content from './Content';
import Footer from './Footer';
import { AuthProvider } from './AuthContext';
function Layout() {
  return (
  <div style={{ backgroundColor: '#f5f5f5', minHeight: '100vh' }}>
    <AntLayout>
     <AuthProvider>
          <Header />
          <AntLayout.Content>
          <Content />
          </AntLayout.Content>
          <Footer />
     </AuthProvider>
    </AntLayout>
   </div>
  );
}

export default Layout;