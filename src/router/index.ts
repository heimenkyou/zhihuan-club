import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import adminRoutes from '@/router/adminRoutes'
import { useAdminStore } from '@/stores/adminStore'
import { ElMessage, ElMessageBox } from 'element-plus'

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: () => import('@/views/Home.vue') },
  {
    path: '/projects',
    name: 'projects',
    component: () => import('@/views/Projects.vue'),
  },
  {
    path: '/competitions',
    name: 'competitions',
    component: () => import('@/views/Competitions.vue'),
    meta: { requiresConfirmation: true }, // 需要确认访问
  },
  {
    path: '/awards',
    name: 'awards',
    component: () => import('@/views/Awards.vue'),
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('@/views/About.vue'),
  },
  {
    path: '/resources',
    name: 'resources',
    component: () => import('@/views/Resources.vue'),
    meta: { requiresConfirmation: true }, // 需要确认访问
  },
  { path: '/join', name: 'join', component: () => import('@/views/Join.vue') },
  {
    path: '/messages',
    name: 'messageList',
    component: () => import('@/views/MessageList.vue'),
  },
  {
    path: '/projectdetail',
    name: 'projectDetail',
    component: () => import('@/views/ProjectDetail.vue'),
  },
  {
    path: '/privacy-policy',
    name: 'privacyPolicy',
    component: () => import('@/views/PrivacyPolicy.vue'),
  },
  {
    path: '/terms-of-service',
    name: 'termsOfService',
    component: () => import('@/views/TermsOfService.vue'),
  },
  // 添加管理员公共路由
  ...adminRoutes,
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    // 如果有保存的位置（返回操作），则恢复到之前的位置
    if (savedPosition) {
      return savedPosition
    }
    // 新页面导航，滚动到顶部
    return { top: 0 }
  },
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const adminStore = useAdminStore()

  // 检查是否需要访问确认
  if (to.meta.requiresConfirmation) {
    try {
      await ElMessageBox.confirm(
        '这个页面还在开发中，页面上还有很多假信息，确认访问吗？',
        '这里没啥东西啊',
        {
          confirmButtonText: '让我康康!',
          cancelButtonText: '溜了',
          type: 'warning',
          // 可选：防止频繁弹窗
          closeOnClickModal: false,
          closeOnPressEscape: false,
        }
      )
      // 用户点击"确定"：继续后续逻辑
    } catch {
      // 用户点击"取消"或关闭
      ElMessage.info('已取消访问')
      next(false) // 阻止导航
      return
    }
  }

  // 检查登录状态 - 本地有token就算登录
  if (to.meta.requiresAuth) {
    // 如果是管理员路由且未登录，尝试验证token
    if (to.path.startsWith('/admin')) {
      if (!adminStore.isLoggedIn) {
        ElMessage.error('请先登录')
        next('/admin/login')
        return
      }
    }
  }

  // 检查是否为超级管理员
  if (to.meta.requiresSuperAdmin && !adminStore.isSuperAdmin()) {
    ElMessage.error('无权限访问此页面')
    next(from.path || '/admin/dashboard')
    return
  }
  next()
})

export default router
