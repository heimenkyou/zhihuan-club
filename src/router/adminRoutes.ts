import type { RouteRecordRaw } from 'vue-router';
import AdminLayout from '../views/admin/layout/AdminLayout.vue';
import Dashboard from '../views/admin/Dashboard.vue';
import ApplicationList from '../views/admin/applications/ApplicationList.vue';
import AwardManagement from '../views/admin/awards/AwardManagement.vue';
import MessageManagement from '../views/admin/messages/MessageManagement.vue';
import AdminManagement from '../views/admin/admins/AdminManagement.vue'; // 新增导入

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
        meta: { title: '管理员管理', requiresSuperAdmin: true }, // 只有超级管理员可访问
      },
    ],
  },
  {
    path: '/admin/login',
    component: () => import('../views/admin/Login.vue'),
    meta: { title: '管理员登录' },
  },
];

export default adminRoutes;