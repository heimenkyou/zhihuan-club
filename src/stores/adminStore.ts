import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Admin } from '@/services/adminService'
import api from '@/services/api'

export const useAdminStore = defineStore('admin', () => {
  const userInfo = ref<Admin | null>(null)
  const isLoading = ref(false)

  // 登录状态 = 本地有token
  const isLoggedIn = computed(() => !!localStorage.getItem('adminToken'))

  // 初始化时设置请求头
  const initAuthState = () => {
    const token = localStorage.getItem('adminToken')
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }

  const login = (userData: Admin, token?: string) => {
    userInfo.value = userData
    if (token) {
      localStorage.setItem('adminToken', token)
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }

  /**
   * 清除认证状态 - 统一处理认证状态清理
   */
  const clearAuthState = () => {
    userInfo.value = null
    localStorage.removeItem('adminToken')
    delete api.defaults.headers.common['Authorization']
  }

  /**
   * 退出登录 - 清除用户状态和token
   * 注意：路由跳转应在组件中处理，避免在store中引入路由依赖
   */
  const logout = () => {
    clearAuthState()
  }

  /**
   * 获取用户信息 - 静默获取，失败时清除状态
   */
  const fetchUserInfo = async () => {
    try {
      const response = await api.get('/admin/admins/me')
      userInfo.value = response.data.data
      return true
    } catch (error) {
      // 获取失败时清除状态，让拦截器处理跳转
      userInfo.value = null
      throw new Error('获取用户信息失败:' + error)
    }
  }

  /**
   * 检查登录状态 - 带加载状态和错误提示
   * 用于需要用户反馈的场景
   */
  const checkLoginStatus = async () => {
    console.log('checkLoginStatus')
    if (isLoading.value) return

    isLoading.value = true
    try {
      await fetchUserInfo()
    } catch (error) {
      console.error('验证登录状态失败:', error)
      // 验证失败时由调用方处理错误提示
    } finally {
      isLoading.value = false
    }
  }

  // 检查是否为超级管理员
  const isSuperAdmin = () => {
    return userInfo.value?.role === 'super'
  }

  // 权限检查方法
  const hasPermission = (permission: string) => {
    // 超级管理员拥有所有权限
    if (userInfo.value?.role === 'super') {
      return true
    }
    // 普通管理员权限控制
    const normalAdminPermissions = ['viewApplications', 'viewAwards']
    return normalAdminPermissions.includes(permission)
  }

  const debug = () => {
    console.log('登录状态:', isLoggedIn.value)
    console.log('用户信息:', userInfo.value)
    console.log('Token是否存在:', !!localStorage.getItem('adminToken'))
    console.log('是否为超级管理员:', isSuperAdmin())
  }

  return {
    isLoggedIn,
    userInfo,
    isLoading,
    initAuthState,
    fetchUserInfo,
    login,
    logout,
    clearAuthState,
    checkLoginStatus,
    isSuperAdmin,
    hasPermission,
    debug,
  }
})
