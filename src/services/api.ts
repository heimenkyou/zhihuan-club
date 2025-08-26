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
      // 处理401未授权
      if (error.response.status === 401) {
        localStorage.removeItem('adminToken');
        // 跳转到登录页
        window.location.href = '/admin/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;