import { createRouter, createWebHistory } from 'vue-router'
import adminRoutes from '@/router/adminRoutes'
import { useAdminStore } from '@/stores/adminStore'
import { ElLoading, ElMessage, ElMessageBox } from 'element-plus'

const routes = [
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
    meta: { requiresConfirmation: true },
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
    meta: { requiresConfirmation: true },
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
    // 如果有保存的位置（返回操作），则恢复到之前的位置。
    if (savedPosition) {
      return savedPosition
    }
    // 新页面导航时回到顶部，避免带入上一页滚动位置。
    return { top: 0 }
  },
})

let routeLoading

/**
 * 在进入受限页面前统一处理访问确认、登录态与权限校验。
 */
router.beforeEach(async (to, from, next) => {
  const adminStore = useAdminStore()

  if (to.meta.requiresConfirmation) {
    try {
      await ElMessageBox.confirm(
        '这个页面还在开发中，页面上还有很多假信息，确认访问吗？',
        '这里没啥东西啊',
        {
          confirmButtonText: '让我康康!',
          cancelButtonText: '溜了',
          type: 'warning',
          closeOnClickModal: false,
          closeOnPressEscape: false,
        }
      )
    } catch {
      ElMessage.info('已取消访问')
      next(false)
      return
    }
  }

  if (to.meta.requiresAuth && to.path.startsWith('/admin')) {
    await adminStore.initialize()
    if (!adminStore.isLoggedIn) {
      ElMessage.error('请先登录')
      next('/admin/login')
      return
    }
  }

  if (to.meta.requiresSuperAdmin && !adminStore.isSuperAdmin()) {
    ElMessage.error('无权限访问此页面')
    next(from.path || '/admin/dashboard')
    return
  }

  next()
})

router.beforeResolve(() => {
  routeLoading = ElLoading.service({
    lock: true,
    text: '页面加载中...',
    background: 'rgb(255 255 255 / 45%)',
  })
})

router.afterEach(() => {
  routeLoading?.close()
  routeLoading = undefined
})

router.onError(() => {
  routeLoading?.close()
  routeLoading = undefined
})

export default router
