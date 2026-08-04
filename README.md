# 智环社团招新门户网站

这是一个基于 Vue 3 + Spring Boot 的智环社团招新与展示系统，主要提供项目、荣誉展示、Markdown 新手导航、招新报名登记和后台管理功能。

## 技术栈

### 前端 (frontend)
- **核心框架**: Vue 3 (Composition API) + JavaScript + Vite 7
- **状态与路由**: Pinia + Vue Router
- **UI 组件库**: Element-Plus
- **样式与排版**: Tailwind CSS 4 + Sass
- **富文本渲染**: md-editor-v3
- **网络请求**: Axios

### 后端 (backend)
- **核心框架**: Spring Boot 3.5 + Java 21
- **持久层**: MyBatis-Plus + MySQL
- **权限与认证**: Sa-Token
- **缓存与性能**: Redis
- **对象存储**: 七牛云 (用于图片与附件云端存储及 CDN 加速)
- **接口文档**: Knife4j + SpringDoc (OpenAPI 3)
- **常用工具库**: Hutool, Fastjson

## 核心功能模块

### 前台功能与页面 (User Views)
面向普通访客与社团意向成员，提供完整的展示与交互体验：
- **首页 & 关于 (Home / About)**: 概览社团介绍与发展历程。
- **项目展示 (Projects / ProjectDetail)**: 列表及详情展示社团成员研发的各类项目作品。
- **荣誉榜 (Awards)**: 按年份展示社团成员在竞赛中获得的奖项。
- **新手导航 (Resources)**: 通过本地 Markdown 文件维护工具福利、竞赛节点和自学线索，由 `md-editor-v3` 预览渲染。
- **加入我们 (Join)**: 核心招新入口，在线填写个人信息并提交报名表。支持通过专业映射表快速选择专业。
- **留言板 (MessageList)**: 访客留言互动，支持点赞及基于 IP 校验的本人留言删除。

### 后台管理系统 (Admin Views)
提供给社团管理者的控制面板，实现数据与内容的完全动态维护：
- **工作台 & 设置 (Dashboard / Profile)**: 查看系统概览状态与当前管理员信息维护。
- **权限管理 (Admins)**: 管理员账号的添加、删除与权限分配。
- **招新审批 (Applications)**: 分页查询、检索、查阅及删除用户的报名表单。
- **内容维护 (Projects / Awards / Messages)**: 动态管理官网展示的项目、奖项以及留言数据。
- **媒体资源中心**: 集中管理系统所有的图片与附件上传，支持查找并清理未被业务引用的冗余文件。

## 快速启动

1. **后端环境**:
   - 准备并启动 MySQL、Redis 服务。
   - 在 `backend/src/main/resources` 目录中配置相应的数据库连接、Redis 及七牛云凭证信息。
   - 使用 Maven 构建或直接运行 `Application` 引导类启动 Spring Boot 服务。

2. **前端开发**:
   - 进入 `frontend` 目录。
   - 执行依赖安装：`pnpm install`。
   - 启动本地开发服务器：`pnpm run dev`。
