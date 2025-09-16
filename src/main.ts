import '@/style.css'

// Font Awesome
import { library } from '@fortawesome/fontawesome-svg-core'
// 导入整个图标集（不是单个图标！）
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// 将整个图标集加入库中
library.add(fas, fab) // fas = 所有实心图标，fab = 所有品牌图标

import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 引入中文语言包

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

app.component('font-awesome-icon', FontAwesomeIcon)

// 使用插件
app.use(router)
app.use(createPinia())
app.use(ElementPlus, {
  locale: zhCn, // 配置中文语言包
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
  // 阻止默认事件处理
  event.preventDefault()
})

// 挂载应用
app.mount('#app')
