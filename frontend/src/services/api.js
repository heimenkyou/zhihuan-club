import axios from 'axios'
import qs from 'qs'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  paramsSerializer: {
    serialize: params => qs.stringify(params, { arrayFormat: 'repeat' }),
  },
})

// 请求拦截器
api.interceptors.request.use(
  config => {
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
    if (!data || typeof data !== 'object') {
      throw new Error('数据格式错误')
    }
    const { code, message } = data

    // token 相关错误码统一走登录失效处理，避免各页面各自兜底。
    const tokenErrorCodes = ['A000130', 'A000131']

    if (tokenErrorCodes.includes(code)) {
      localStorage.removeItem('adminToken')
      window.dispatchEvent(new CustomEvent('admin-auth-expired'))
      return Promise.reject(new Error(message || '登录已失效，请重新登录'))
    }

    if (code === '0') {
      return response
    }

    return Promise.reject(new Error(message || '请求失败'))
  },
  error => {
    const message = getErrorMessage(error)

    if (error.response?.status === 401) {
      localStorage.removeItem('adminToken')
      window.dispatchEvent(new CustomEvent('admin-auth-expired'))
    }

    return Promise.reject(new Error(message))
  }
)

/**
 * 将底层请求异常转换为面向用户的统一提示文案。
 */
const getErrorMessage = error => {
  if (!error) return '未知错误'

  if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
    return '请求超时，请检查网络'
  }

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
