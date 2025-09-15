import './style.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


// 导入Vue 3版本的Markdown编辑器
import VMdEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'

// 引入Prism
import Prism from 'prismjs'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-css'
import 'prismjs/components/prism-markup'

// 引入你封装的提示函数
import { showError } from './utils/notification'

// 创建应用实例
export const app = createApp(App)

// 配置主题
VMdEditor.use(githubTheme, { Prism })

// 注册Markdown编辑器组件
app.component('VMdEditor', VMdEditor)
app.component('v-md-editor', VMdEditor) // 同时注册kebab-case形式

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用插件
app.use(router)
app.use(createPinia())
app.use(ElementPlus)

// // 成功操作提示工具函数
// export const showSuccessNotification = (message: string) => {
//   ElNotification({
//     title: '操作成功',
//     message,
//     type: 'success',
//     duration: 2000, // 2秒后自动关闭
//     icon: '✓', // 使用字符图标
//     position: 'top-right', // 右上角
//     customClass: 'fade-effect', // 淡入动画效果类名
//   })
// }

// // 错误操作提示工具函数
// export const showErrorNotification = (message: string) => {
//   ElNotification({
//     title: '操作失败',
//     message,
//     type: 'error',
//     duration: 3000,
//   })
// }

// Vue 全局异常处理器
app.config.errorHandler = (err, _instance, info) => {
  console.error('Vue错误:', err, info)
  showError(err instanceof Error ? err.message : String(err))

  // // 显示错误提示
  // ElNotification({
  //   title: '错误',
  //   message: err instanceof Error ? err.message : String(err),
  //   type: 'error',
  //   duration: 3000,
  //   position: 'top-right'
  // })
}

// Promise 未捕获异常处理器
window.addEventListener('unhandledrejection', event => {
  console.error('未捕获的Promise错误:', event.reason)
  showError(
    event.reason instanceof Error ? event.reason.message : String(event.reason)
  )

  // // 显示错误提示
  // ElNotification({
  //   title: '错误',
  //   message: event.reason instanceof Error ? event.reason.message : String(event.reason),
  //   type: 'error',
  //   duration: 3000,
  //   position: 'top-right'
  // })

  // 阻止默认事件处理
  event.preventDefault()
})

// 挂载应用
app.mount('#app')
