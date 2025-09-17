import axios from 'axios'

export interface Result<T> {
  code: string
  message: string
  data: T
  requestId?: string
  success: boolean
  fail: boolean
}

// 分页响应类型定义
export interface PageData<T> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}
// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 根据实际情况设置
  timeout: 10000, // 设置超时时间
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 添加token到请求头
    const token = localStorage.getItem('adminToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const data = response.data
    // 如果响应数据格式异常，直接抛错
    if (!data || typeof data !== 'object') {
      throw new Error('数据格式错误')
    }
    const { code, message } = data
    // 业务成功
    if (code === '0') {
      return response
    }
    // 定义 token 相关错误码
    const tokenErrorCodes = ['A000130', 'A000131'] // token 为空、无效或过期
    if (tokenErrorCodes.includes(code)) {
      // 视为未登录或 token 失效，触发登出流程
      if (!window.location.pathname.includes('/admin/login')) {
        setTimeout(() => {
          localStorage.removeItem('adminToken')
          window.location.href = '/admin/login'
        }, 1000)
      }
      return Promise.reject(new Error(message || '登录已失效，请重新登录'))
    }

    // 其他业务错误
    return Promise.reject(new Error(message || '请求失败'))
  },
  error => {
    // HTTP 状态码层面的错误（如网络失败、500、401等）

    // 特殊处理：HTTP 401，作为兜底（比如某些网关直接返回401）
    if (error.response?.status === 401) {
      if (!window.location.pathname.includes('/admin/login')) {
        setTimeout(() => {
          localStorage.removeItem('adminToken')
          window.location.href = '/admin/login'
        }, 1000)
      }
      return Promise.reject(new Error('登录已失效，请重新登录'))
    }

    // 其他网络或服务器错误
    const msg = error.message || '网络错误'
    return Promise.reject(new Error(msg))
  }
)

export default api
