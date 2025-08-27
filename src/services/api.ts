import axios from 'axios';

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 根据实际情况设置
  timeout: 5000, // 设置超时时间
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 添加token到请求头
    const token = localStorage.getItem('adminToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    // 统一处理响应格式
    return response;
  },
  (error) => {
    // 统一处理错误
    if (error.response) {
      // 处理401未授权 - 改进版本
      if (error.response.status === 401) {
        // 检查当前路径是否已经是登录页，避免无限循环
        if (!window.location.pathname.includes('/admin/login')) {
          // 延迟清除token和跳转，让用户看到错误提示
          setTimeout(() => {
            localStorage.removeItem('adminToken');
            window.location.href = '/admin/login';
          }, 1000);
        }
      }
    }
    return Promise.reject(error);
  }
);

export default api;