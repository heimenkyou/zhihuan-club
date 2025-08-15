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
    // 检查response.data是否有success字段
    const responseData = response.data as any
    if ('success' in responseData && !responseData.success && responseData.message) {
      // 直接返回被拒绝的Promise，让全局异常处理器处理
      return Promise.reject(new Error(responseData.message))
    }
    
    // 如果success为true或没有success字段，直接返回响应数据
    return responseData
  },
  (error) => {
    // 错误处理
    console.error('API请求错误:', error.response?.status || error.message)
    
    // 检查是否有response和data
    if (error.response && error.response.data) {
      const responseData = error.response.data as any
      
      // 检查是否有message字段
      if (responseData.message) {
        // 使用响应中的message创建新的Error
        return Promise.reject(new Error(responseData.message))
      }
    }
    
    // 没有特定错误信息，使用原始错误
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