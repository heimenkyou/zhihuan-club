import '@/style.css'

// Font Awesome
import { library } from '@fortawesome/fontawesome-svg-core'
// 导入整个图标集
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import {far} from '@fortawesome/free-regular-svg-icons' 
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { dom } from '@fortawesome/fontawesome-svg-core'

// 启用自动替换 <i class="fas fa-home"> → SVG
dom.watch()

// 将整个图标集加入库中
library.add(fas, fab, far) // fas = 所有实心图标，fab = 所有品牌图标，far = 线框

import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 引入中文语言包

// 导入md-editor-v3 Markdown编辑器
import { MdEditor, MdPreview, MdCatalog } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

// 引入你封装的提示函数
import { showError } from './utils/notification'

// 创建应用实例
export const app = createApp(App)

// 注册md-editor-v3组件
app.component('MdEditor', MdEditor)
app.component('MdPreview', MdPreview)
app.component('MdCatalog', MdCatalog)

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.component('font-awesome-icon', FontAwesomeIcon)

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn, // 配置中文语言包
})

// 初始化认证状态（必须在pinia和router之后）
import { useAdminStore } from '@/stores/adminStore'
const adminStore = useAdminStore()
adminStore.initAuthState()

// 如果本地有token但用户信息为空，获取用户信息
if (localStorage.getItem('adminToken') && !adminStore.userInfo) {
  adminStore.fetchUserInfo()
}

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
