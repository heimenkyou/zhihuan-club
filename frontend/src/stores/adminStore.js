import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'

export const useAdminStore = defineStore('admin', () => {
  const userInfo = ref(null)
  const isLoading = ref(false)

  // 登录状态 = 本地有 token
  const isLoggedIn = computed(() => !!localStorage.getItem('adminToken'))

  /**
   * 写入当前登录用户与 token，保证刷新后仍可恢复登录态。
   */
  const login = (userData, token) => {
    userInfo.value = userData
    if (token) {
      localStorage.setItem('adminToken', token)
    }
  }

  /**
   * 清除认证状态，统一处理本地登录信息回收。
   */
  const clearAuthState = () => {
    userInfo.value = null
    localStorage.removeItem('adminToken')
  }

  /**
   * 退出登录，仅负责清理状态，路由跳转留给页面层处理。
   */
  const logout = () => {
    clearAuthState()
  }

  /**
   * 静默拉取当前用户信息，失败时回收无效登录态。
   */
  const fetchUserInfo = async () => {
    try {
      const response = await api.get('/admin/admins/me')
      userInfo.value = response.data.data
      return true
    } catch (error) {
      userInfo.value = null
      throw new Error('获取用户信息失败:' + error)
    }
  }

  /**
   * 主动校验当前登录状态，避免重复并发请求。
   */
  const checkLoginStatus = async () => {
    console.log('检查登录状态...')
    if (isLoading.value) return

    isLoading.value = true
    try {
      await fetchUserInfo()
      return true
    } catch (error) {
      console.error('验证登录状态失败:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  /**
   * 判断当前用户是否具备超级管理员权限。
   */
  const isSuperAdmin = () => {
    return userInfo.value?.role === 'super'
  }

  /**
   * 按角色判断当前用户是否拥有指定权限。
   */
  const hasPermission = permission => {
    if (userInfo.value?.role === 'super') {
      return true
    }
    const normalAdminPermissions = ['viewApplications', 'viewAwards']
    return normalAdminPermissions.includes(permission)
  }

  /**
   * 输出当前认证状态，便于排查登录态异常。
   */
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
