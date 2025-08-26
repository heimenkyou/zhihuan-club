import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Admin } from '../services/adminService';
import { getCurrentAdmin } from '../services/adminService';
import api from '../services/api'; // 请根据实际项目路径调整导入路径
import { useRouter } from 'vue-router';

export const useAdminStore = defineStore('admin', () => {
  const isLoggedIn = ref(false);
  const userInfo = ref<Admin | null>(null);
  const isLoading = ref(false); // 添加加载状态

  const login = (userData: Admin) => {
    isLoggedIn.value = true;
    userInfo.value = userData;
    // 移除硬编码的dummy-token
    // 实际项目中token应来自服务器响应
  };

  const logout = () => {
    isLoggedIn.value = false;
    userInfo.value = null;
    localStorage.removeItem('adminToken');
    // 添加重定向到登录页
    // 由于未找到router变量，推测需要引入vue-router
    // 实际使用时需要确保项目中已正确配置vue-router
    const router = useRouter();
    router.push('/admin/login');
  };

  // 检查是否已登录
  const checkLoginStatus = async () => {
    // 如果已经在加载中，则不再重复请求
    if (isLoading.value) return;
    
    const token = localStorage.getItem('adminToken');
    if (token) {
      isLoading.value = true;
      try {
        // 设置请求头中的Authorization
// 由于找不到 api 变量，推测需要引入 axios 实例，以下示例假设从 api 模块导入
api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // 获取当前管理员信息
        const adminInfo = await getCurrentAdmin();
        isLoggedIn.value = true;
        userInfo.value = adminInfo;
      } catch (error) {
        console.error('验证登录状态失败:', error);
        // 清除无效token
        logout();
      } finally {
        isLoading.value = false;
      }
    } else {
      // 没有token，确保状态为未登录
      isLoggedIn.value = false;
      userInfo.value = null;
    }
  };

  // 检查是否为超级管理员
  const isSuperAdmin = () => {
    return userInfo.value?.role === 'super';
  };

  // 权限检查方法
  const hasPermission = (permission: string) => {
    // 超级管理员拥有所有权限
    if (userInfo.value?.role === 'super') {
      return true;
    }
    // 普通管理员权限控制
    const normalAdminPermissions = ['viewApplications', 'viewAwards'];
    return normalAdminPermissions.includes(permission);
  };

  const debug = () => {
    console.log('登录状态:', isLoggedIn.value);
    console.log('用户信息:', userInfo.value);
    console.log('Token是否存在:', !!localStorage.getItem('adminToken'));
    console.log('是否为超级管理员:', isSuperAdmin());
  };

  return {
    isLoggedIn,
    userInfo,
    isLoading,
    login,
    logout,
    checkLoginStatus,
    isSuperAdmin,
    hasPermission,
    debug,
  };
});