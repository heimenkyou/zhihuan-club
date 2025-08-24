import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAdminStore = defineStore('admin', () => {
  const isLoggedIn = ref(false);
  const userInfo = ref<{ username: string } | null>(null);

  const login = (userData: { username: string }) => {
    isLoggedIn.value = true;
    userInfo.value = userData;
    localStorage.setItem('adminToken', 'dummy-token'); // 实际项目中应存储真实token
  };

  const logout = () => {
    isLoggedIn.value = false;
    userInfo.value = null;
    localStorage.removeItem('adminToken');
  };

  // 检查是否已登录
  const checkLoginStatus = () => {
    const token = localStorage.getItem('adminToken');
    if (token) {
      // 实际项目中应验证token有效性
      isLoggedIn.value = true;
      userInfo.value = { username: 'admin' };
    }
  };

  const debug = () => {
    console.log('登录状态:', isLoggedIn.value);
    console.log('用户信息:', userInfo.value);
    console.log('Token是否存在:', !!localStorage.getItem('adminToken'));
  };
  
  return {
    isLoggedIn,
    userInfo,
    login,
    logout,
    checkLoginStatus,
    debug,
  };
});