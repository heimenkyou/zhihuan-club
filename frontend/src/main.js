import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import { useAdminStore } from '@/stores/adminStore'
import { showError } from '@/utils/notification'
import '@/style.css'

export const app = createApp(App)

// 使用插件
app.use(createPinia())
app.use(router)

const adminStore = useAdminStore()

window.addEventListener('admin-auth-expired', () => {
  adminStore.clearAuthState()
  if (router.currentRoute.value.path.startsWith('/admin')) {
    router.replace('/admin/login')
  }
})

// Vue 全局异常处理器
app.config.errorHandler = (err, _instance, info) => {
  console.error('Vue错误:', err, info)
  showError(err instanceof Error ? err.message : String(err))
}

// Promise 未捕获异常处理器
window.addEventListener('unhandledrejection', event => {
  console.error('未捕获的Promise错误:', event.reason)
  showError(
    event.reason instanceof Error ? event.reason.message : String(event.reason)
  )
  // 阻止默认事件处理，避免同一错误重复打断用户。
  event.preventDefault()
})

// 挂载应用
app.mount('#app')
