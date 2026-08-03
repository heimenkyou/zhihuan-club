import '@/style.css'

// Font Awesome
import { library } from '@fortawesome/fontawesome-svg-core'
// 导入整个图标集
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// 将整个图标集加入库中
library.add(fas, fab, far) // fas = 所有实心图标，fab = 所有品牌图标，far = 线框

import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import {
  ArrowDown,
  ArrowLeft,
  ArrowRight,
  Box,
  CircleCheck,
  Cpu,
  Document,
  Edit,
  Finished,
  House,
  Link,
  Message,
  Picture,
  Plus,
  Refresh,
  SwitchButton,
  Trophy,
  User,
} from '@element-plus/icons-vue'

// 导入md-editor-v3 Markdown编辑器
import { MdEditor, MdPreview, MdCatalog } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

// 引入你封装的提示函数
import { showError } from './utils/notification'
import { useAdminStore } from '@/stores/adminStore'

// 创建应用实例
export const app = createApp(App)

// 注册md-editor-v3组件
app.component('MdEditor', MdEditor)
app.component('MdPreview', MdPreview)
app.component('MdCatalog', MdCatalog)

// 仅注册模板中未显式导入的 Element Plus 图标，避免打包整套图标库。
const elementIcons = {
  ArrowDown,
  ArrowLeft,
  ArrowRight,
  Box,
  CircleCheck,
  Cpu,
  Document,
  Edit,
  Finished,
  House,
  Link,
  Message,
  Picture,
  Plus,
  Refresh,
  SwitchButton,
  Trophy,
  User,
}
for (const [name, component] of Object.entries(elementIcons)) {
  app.component(name, component)
}

app.component('font-awesome-icon', FontAwesomeIcon)

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
