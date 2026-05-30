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

### 面向用户 (前台)
- **招新报名**: 在线填写报名信息提交。
- **留言板**: 查看留言、发表留言、点赞及取消点赞。支持基于 IP 校验进行本人留言删除。
- **项目与奖项展示**: 浏览社团已开发的项目及所获荣誉。
- **考核提交**: 提供代码提交 (CodeSubmission) 功能，用于招新技术考核。

### 后台管理 (Admin)
- **系统权限**: 管理员账号的登录、登出及增删改查。
- **报名管理**: 分页查询、检索、删除用户的报名信息。
- **内容维护**: 动态管理展示的项目内容、奖项信息与留言数据。
- **媒体资源中心**: 集中管理 MinIO 文件的上传与删除，支持查找并清理未被业务引用的冗余文件。
- **基础数据映射**: 维护专业代码与名称等字典数据的映射关系。

## 快速启动

1. **后端环境**:
   - 准备并启动 MySQL、Redis、MinIO 服务。
   - 在 `backend/src/main/resources` 目录中配置相应的数据库连接、Redis 及 MinIO 凭证信息。
   - 使用 Maven 构建或直接运行 `Application` 引导类启动 Spring Boot 服务。

2. **前端开发**:
   - 进入 `frontend` 目录。
   - 执行依赖安装：`npm install` (或使用 `pnpm`)。
   - 启动本地开发服务器：`npm run dev`。
