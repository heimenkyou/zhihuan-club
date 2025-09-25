import AdminLayout from '@/views/admin/layout/AdminLayout.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import ApplicationList from '@/views/admin/applications/ApplicationList.vue'
import AwardManagement from '@/views/admin/awards/AwardManagement.vue'
import MessageManagement from '@/views/admin/messages/MessageManagement.vue'
import AdminManagement from '@/views/admin/admins/AdminManagement.vue'
import ProjectManagement from '@/views/admin/projects/ProjectManagement.vue'
import Profile from '@/views/admin/Profile.vue'
import type { RouteRecordRaw } from 'vue-router'

const adminRoutes: RouteRecordRaw[] = [
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        component: Dashboard,
        meta: { title: '仪表盘' },
      },
      {
        path: 'applications',
        component: ApplicationList,
        meta: { title: '报名管理' },
      },
      {
        path: 'awards',
        component: AwardManagement,
        meta: { title: '奖项管理' },
      },
      {
        path: 'messages',
        component: MessageManagement,
        meta: { title: '消息管理' },
      },
      {
        path: 'admins',
        component: AdminManagement,
        meta: { title: '管理员管理', requiresSuperAdmin: true },
      },
      // 项目管理路由
      {
        path: 'projects',
        name: 'ProjectManagement',
        component: ProjectManagement,
        meta: { title: '项目管理' },
      },

      // 项目编辑页面路由
      {
        path: 'projects/edit/:id?',
        name: 'ProjectEdit',
        component: () => import('@/views/admin/projects/ProjectEditPage.vue'),
        meta: { title: '编辑项目' },
      },
      // 个人资料页面路由
      {
        path: 'profile',
        name: 'AdminProfile',
        component: Profile,
        meta: { title: '个人资料' },
      },
    ],
  },
  {
    path: '/admin/login',
    component: () => import('@/views/admin/Login.vue'),
    meta: { title: '管理员登录', requiresAuth: false },
  },
]

export default adminRoutes
