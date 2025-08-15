# 社团招新留言板前端

这是一个基于Vue3 + Vite + TypeScript的留言板应用，用于社团招新活动。

## 功能特性

- 📝 查看留言列表
- 👍 点赞/取消点赞功能
- 🗑️ 删除留言（仅限留言对应的IP）
- 📱 响应式设计
- 🔍 实时调试信息

## 技术栈

- **前端框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **语言**: TypeScript
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **样式**: CSS3

## 项目结构

```
src/
├── components/          # 公共组件
├── views/              # 页面组件
│   ├── Home.vue        # 首页
│   └── MessageList.vue # 留言列表页
├── stores/             # 状态管理
│   └── messageStore.ts # 留言状态管理
├── services/           # API服务
│   ├── api.ts          # HTTP客户端配置
│   └── messageService.ts # 留言相关API
├── router/             # 路由配置
└── assets/             # 静态资源
```

## 开发环境

### 环境要求
- Node.js >= 16
- npm >= 8

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 类型检查
```bash
npx vue-tsc --noEmit
```

## API配置

项目配置了代理来解决跨域问题：

```typescript
// vite.config.ts
server: {
  proxy: {
    '/api': {
      target: 'https://club.luowb.cn',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api', '/api')
    }
  }
}
```

## 类型定义

### 留言数据结构
```typescript
interface MessageItem {
  id: number
  nickname: string
  content: string
  likeCount: number
  createTime: string
  liked: boolean
  canDelete: boolean
}
```

### 分页数据结构
```typescript
interface PageData<T> {
  current: number
  size: number
  total: number
  pages: number
  records: T[]
}
```

### API响应结构
```typescript
interface ApiResponse<T> {
  code: string
  message: string
  data: T
  requestId: string
}
```

## 问题修复记录

### TypeScript类型错误修复

**问题描述**: 
```
类型"ApiResponse<PageData<MessageItem>>"缺少类型"PageData<MessageItem>"中的以下属性: current, size, total, pages, records
```

**问题原因**: 
1. 响应拦截器改变了axios的返回类型，但TypeScript类型系统不知道这一点
2. 类型定义不匹配：函数返回类型与实际API响应结构不一致

**解决方案**:
1. 重新设计API客户端的类型定义，确保类型安全
2. 在响应拦截器中正确处理类型转换
3. 使用泛型来保持类型一致性

**修复后的代码结构**:
```typescript
// 类型安全的API客户端
const api = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return axiosInstance.get(url, config)
  }
  // ... 其他方法
}
```

## 调试功能

项目集成了详细的调试日志，可以在浏览器控制台查看：
- 🚀 请求参数
- 📡 响应数据
- 🔍 数据结构
- ✅ API响应结构
- 📄 分页数据

## 许可证

MIT License
