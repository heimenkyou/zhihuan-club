import './style.css'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus, { ElNotification } from 'element-plus'
import 'element-plus/dist/index.css'
import { createApp } from 'vue'

// 创建应用实例
const app = createApp(App)

// 成功操作提示工具函数
export const showSuccessNotification = (message: string) => {
  ElNotification({
    title: '操作成功',
    message,
    type: 'success',
    duration: 2000, // 2秒后自动关闭
    icon: '✓', // 使用字符图标
    position: 'top-right', // 右上角
    customClass: 'fade-effect' // 淡入动画效果类名
  })
}

// Vue 全局异常处理器
app.config.errorHandler = (err, _instance, info) => {
  console.error('Vue错误:', err, info)
  
  // 显示错误提示
  ElNotification({
    title: '错误',
    message: err instanceof Error ? err.message : String(err),
    type: 'error',
    duration: 3000,
    position: 'top-right'
  })
}

// Promise 未捕获异常处理器
window.addEventListener('unhandledrejection', (event) => {
  console.error('未捕获的Promise错误:', event.reason)
  
  // 显示错误提示
  ElNotification({
    title: '错误',
    message: event.reason instanceof Error ? event.reason.message : String(event.reason),
    type: 'error',
    duration: 3000,
    position: 'top-right'
  })
  
  // 阻止默认事件处理
  event.preventDefault()
})

// 使用插件并挂载应用
app.use(router)
app.use(createPinia())
app.use(ElementPlus)
app.mount('#app')
