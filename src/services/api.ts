import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 5000
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加请求头，如认证token
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    // 错误处理
    console.error('API请求错误:', error.response?.status || error.message)
    return Promise.reject(error)
  }
)

export default api