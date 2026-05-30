# 智环社团招新门户网站

这是一个基于 Vue 3 + Spring Boot 的智环社团招新与展示系统，主要提供项目展示、留言交互、招新报名登记以及完善的后台管理功能。

## 技术栈

### 前端 (frontend)
- **核心框架**: Vue 3 (Composition API) + TypeScript + Vite 7
- **状态与路由**: Pinia + Vue Router
- **UI 组件库**: Element-Plus
- **样式与排版**: Tailwind CSS 4 + Sass
- **富文本渲染**: md-editor-v3
- **网络请求**: Axios

### 后端 (backend)
- **核心框架**: Spring Boot 3.5 + Java 21
- **持久层**: MyBatis-Plus + MySQL
- **缓存与性能**: Redis
- **对象存储**: MinIO (用于管理图片与文件资源)
- **接口文档**: Knife4j + SpringDoc (OpenAPI 3)
- **常用工具库**: Hutool, Fastjson

## 核心功能模块

### 前台功能与页面 (User Views)
面向普通访客与社团意向成员，提供完整的展示与交互体验：
- **首页 & 关于 (Home / About)**: 概览社团介绍、文化及相关学习资源 (Resources)。
- **项目展示 (Projects / ProjectDetail)**: 列表及详情展示社团成员研发的各类项目作品。
- **荣誉与竞赛 (Awards / Competitions)**: 汇总展示社团在各大竞赛中斩获的奖项。
- **加入我们 (Join)**: 核心招新入口，在线填写个人信息并提交报名表。
- **留言板 (MessageList)**: 访客留言互动，支持点赞及基于 IP 校验的本人留言删除。
- **考核提交 (CodeSubmit)**: 提供面向新生的技术考核代码提交入口。

### 后台管理系统 (Admin Views)
提供给社团管理者的控制面板，实现数据与内容的完全动态维护：
- **工作台 & 设置 (Dashboard / Profile)**: 查看系统概览状态与当前管理员信息维护。
- **权限管理 (Admins)**: 管理员账号的添加、删除与权限分配。
- **招新审批 (Applications)**: 分页查询、检索、查阅及删除用户的报名表单。
- **内容维护 (Projects / Awards / Messages)**: 动态管理官网展示的项目、奖项以及留言数据。
- **考核代码 (CodeSubmission)**: 集中查看与管理报名者的考核代码提交记录。
- **媒体资源中心**: 集中管理 MinIO 文件的上传，支持查找并清理未被业务引用的冗余冗余图片或文件。

## 快速启动

1. **后端环境**:
   - 准备并启动 MySQL、Redis、MinIO 服务。
   - 在 `backend/src/main/resources` 目录中配置相应的数据库连接、Redis 及 MinIO 凭证信息。
   - 使用 Maven 构建或直接运行 `Application` 引导类启动 Spring Boot 服务。

2. **前端开发**:
   - 进入 `frontend` 目录。
   - 执行依赖安装：`npm install` (或使用 `pnpm`)。
   - 启动本地开发服务器：`npm run dev`。
