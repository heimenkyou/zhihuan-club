import axios from 'axios'
import type { AxiosResponse, AxiosRequestConfig } from 'axios'

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 可以在这里添加请求头，如认证token
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axiosInstance.interceptors.response.use(
  <T>(response: AxiosResponse<T>) => {
    // 直接返回响应数据，而不是整个response对象
    return response.data
  },
  (error) => {
    // 错误处理
    console.error('API请求错误:', error.response?.status || error.message)
    return Promise.reject(error)
  }
)

// 创建类型安全的API客户端
const api = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.get(url, config)
  },
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.post(url, data, config)
  },
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.put(url, data, config)
  },
  delete: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.delete(url, config)
  }
}

export default api