import router from '@/router'
import axios from 'axios'
import { useAdminStore } from '@/stores/adminStore'

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
    
    // 定义 token 相关错误码
    const tokenErrorCodes = ['A000130', 'A000131'] // token 为空、无效或过期
    
    // token相关错误码，统一处理为登录失效
    if (tokenErrorCodes.includes(code)) {
      const adminStore = useAdminStore()
      const currentPath = router.currentRoute.value.path
      
      adminStore.clearAuthState()
      
      // 只在访问管理员页面时才跳转到登录页
      if (currentPath.startsWith('/admin')) {
        router.replace('/admin/login')
      }
      
      return Promise.reject(new Error(message || '登录已失效，请重新登录'))
    }
    
    // 业务成功
    if (code === '0') {
      return response
    }
    // 其他业务错误
    return Promise.reject(new Error(message || '请求失败'))
  },
  error => {
    const message = getErrorMessage(error)

    // HTTP 401 状态码处理
    if (error.response?.status === 401) {
      const adminStore = useAdminStore()
      const currentPath = router.currentRoute.value.path
      
      adminStore.clearAuthState()
      
      // 只在访问管理员页面时才跳转到登录页
      if (currentPath.startsWith('/admin')) {
        router.replace('/admin/login')
      }
    }

    return Promise.reject(new Error(message))
  }
)

const getErrorMessage = (error: any) => {
  if (!error) return '未知错误'

  // 超时
  if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
    return '请求超时，请检查网络'
  }

  // 无响应（断网、DNS 失败等）
  if (!error.response) {
    return '网络连接失败，请检查网络设置'
  }

  const status = error.response.status

  switch (status) {
    case 401:
      return '登录已失效，请重新登录'
    case 403:
      return '权限不足，无法访问该资源'
    case 404:
      return '请求的资源不存在'
    case 500:
      return '服务器内部错误'
    case 502:
    case 503:
    case 504:
      return '服务暂时不可用，请稍后重试'
    default:
      return `请求失败 (${status})`
  }
}

export default api
